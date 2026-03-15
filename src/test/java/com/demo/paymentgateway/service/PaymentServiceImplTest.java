package com.demo.paymentgateway.service;

import com.demo.paymentgateway.dto.PaymentRequest;
import com.demo.paymentgateway.entity.PaymentMethod;
import com.demo.paymentgateway.entity.PaymentStatus;
import com.demo.paymentgateway.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PaymentServiceImplTest {

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(mock(PaymentTransactionRepository.class));
    }

    @Test
    void shouldMarkPaymentFailedWhenSourceContainsFail() {
        PaymentRequest request = buildRequest("card-fail-0001", new BigDecimal("500.00"));

        assertEquals(PaymentStatus.FAILED, paymentService.simulateStatus(request));
    }

    @Test
    void shouldMarkPaymentPendingForHighValueTransactions() {
        PaymentRequest request = buildRequest("upi-demo@bank", new BigDecimal("50000.00"));

        assertEquals(PaymentStatus.PENDING, paymentService.simulateStatus(request));
    }

    @Test
    void shouldMarkPaymentSuccessfulForNormalTransactions() {
        PaymentRequest request = buildRequest("4111111111111111", new BigDecimal("1299.00"));

        assertEquals(PaymentStatus.SUCCESS, paymentService.simulateStatus(request));
    }

    private PaymentRequest buildRequest(String source, BigDecimal amount) {
        PaymentRequest request = new PaymentRequest();
        request.setMerchantOrderId("ORDER-1001");
        request.setAmount(amount);
        request.setCurrency("INR");
        request.setPaymentMethod(PaymentMethod.CARD);
        request.setPaymentSource(source);
        return request;
    }
}
