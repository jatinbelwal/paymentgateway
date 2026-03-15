package com.demo.paymentgateway.service;

import com.demo.paymentgateway.dto.PaymentRequest;
import com.demo.paymentgateway.dto.PaymentResponse;
import com.demo.paymentgateway.entity.PaymentStatus;
import com.demo.paymentgateway.entity.PaymentTransaction;
import com.demo.paymentgateway.exception.DuplicateResourceException;
import com.demo.paymentgateway.exception.ResourceNotFoundException;
import com.demo.paymentgateway.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentTransactionRepository repository;

    public PaymentServiceImpl(PaymentTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        repository.findByMerchantOrderId(request.getMerchantOrderId()).ifPresent(existing -> {
            throw new DuplicateResourceException("Payment already exists for merchantOrderId: " + request.getMerchantOrderId());
        });

        PaymentStatus status = simulateStatus(request);

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setMerchantOrderId(request.getMerchantOrderId());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setPaymentMethod(request.getPaymentMethod());
        transaction.setStatus(status);
        transaction.setTransactionReference(generateReference());

        if (status == PaymentStatus.FAILED) {
            transaction.setFailureReason("Simulation rule triggered a payment failure.");
        } else if (status == PaymentStatus.PENDING) {
            transaction.setFailureReason("Payment marked pending for manual confirmation.");
        }

        return toResponse(repository.save(transaction));
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        PaymentTransaction transaction = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for id: " + id));
        return toResponse(transaction);
    }

    @Override
    public PaymentResponse getPaymentByMerchantOrderId(String merchantOrderId) {
        PaymentTransaction transaction = repository.findByMerchantOrderId(merchantOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for merchantOrderId: " + merchantOrderId));
        return toResponse(transaction);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentStatus simulateStatus(PaymentRequest request) {
        String source = request.getPaymentSource().toUpperCase(Locale.ROOT);

        if (source.contains("FAIL") || source.endsWith("0000")) {
            return PaymentStatus.FAILED;
        }

        if (request.getAmount().doubleValue() >= 50000 || source.contains("PENDING")) {
            return PaymentStatus.PENDING;
        }

        return PaymentStatus.SUCCESS;
    }

    private PaymentResponse toResponse(PaymentTransaction transaction) {
        PaymentResponse response = new PaymentResponse();
        response.setId(transaction.getId());
        response.setMerchantOrderId(transaction.getMerchantOrderId());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setPaymentMethod(transaction.getPaymentMethod());
        response.setStatus(transaction.getStatus());
        response.setTransactionReference(transaction.getTransactionReference());
        response.setFailureReason(transaction.getFailureReason());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }

    private String generateReference() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase(Locale.ROOT);
    }
}
