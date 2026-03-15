package com.demo.paymentgateway.repository;

import com.demo.paymentgateway.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByMerchantOrderId(String merchantOrderId);
}
