package com.bridgeline.authorize.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "PaymentTransaction")
public class PaymentTransaction {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "status")
    private String status;

    @Column(name = "authorization_code")
    private String authorizationCode;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "customer_account_id")
    private String customerAccountId;

    @Column(name = "vendor_id")
    private Integer vendorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "card_id")
    private CardDetails cardDetails;

}
