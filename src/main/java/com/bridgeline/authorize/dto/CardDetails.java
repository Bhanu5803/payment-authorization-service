package com.bridgeline.authorize.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDetails {
    @NotNull
    private String cardNumber;
    @NotNull
    private String expirationDate;
    @NotNull
    private String cardholderName;
    @NotNull
    @Size(max = 3)
    private String cvv;
    @NotNull
    private String cardName;
}
