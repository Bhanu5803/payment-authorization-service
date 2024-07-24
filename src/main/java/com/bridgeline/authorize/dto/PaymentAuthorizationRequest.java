package com.bridgeline.authorize.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentAuthorizationRequest {
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
   private CardDetails cardDetails;
}
