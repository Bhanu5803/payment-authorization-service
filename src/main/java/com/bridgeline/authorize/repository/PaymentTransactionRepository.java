package com.bridgeline.authorize.repository;

import com.bridgeline.authorize.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction,Integer > {
}
