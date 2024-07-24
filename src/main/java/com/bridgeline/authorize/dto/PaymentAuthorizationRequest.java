package com.bridgeline.authorize.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String currency;
    @NotNull
    private String paymentMethod;
    @NotNull
    private CardDetails cardDetails;
}
