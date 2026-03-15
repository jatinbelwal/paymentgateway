package com.demo.paymentgateway.service;

import com.demo.paymentgateway.dto.PaymentRequest;
import com.demo.paymentgateway.dto.PaymentResponse;
import com.demo.paymentgateway.entity.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByMerchantOrderId(String merchantOrderId);
    List<PaymentResponse> getAllPayments();
    PaymentStatus simulateStatus(PaymentRequest request);
}
