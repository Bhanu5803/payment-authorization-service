package com.bridgeline.authorize.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailsWithAdditionalData {
    private CardDetails cardDetails;
    private BigDecimal amount;
    private String currency;
}
