package com.bridgeline.authorize.repository;

import com.bridgeline.authorize.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Integer > {

    List<PaymentTransaction> findByAmount(BigDecimal amount);

    List<PaymentTransaction> findByCurrency(String currency);

    List<PaymentTransaction> findByAmountOrCurrency(BigDecimal amount, String currency);
}
