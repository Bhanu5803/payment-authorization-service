package com.bridgeline.authorize.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDetails {
    private String cardNumber;
    private String expirationDate;
    private String cardholderName;
    private String cvv;
    private String cardName;
}
