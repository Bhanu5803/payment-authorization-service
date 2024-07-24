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
@Table(name="CardType")
public class CardType {
    @Id
    @Column(name="card_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cardTypeId;
    @Column(name="card_name")
    private String cardName;
    @Column(name="card_id")
    private Integer cardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "card_id",insertable=false, updatable=false)
    private CardDetails cardDetails;
}
