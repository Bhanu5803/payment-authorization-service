package com.bridgeline.authorize.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CardDetails")
public class CardDetails {
    @Id
    @Column(name="card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer cardId;
    @Column(name="card_number")
   private String cardNumber;
    @Column(name="expiration_date")
   private String expirationDate;
    @Column(name="cardholder_name")
   private String cardholderName;
    @Column(name="cvv")
   private String cvv;

}
