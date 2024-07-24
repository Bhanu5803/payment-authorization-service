package com.bridgeline.authorize.service;


import com.bridgeline.authorize.domain.CardDetails;

import com.bridgeline.authorize.domain.CardType;
import com.bridgeline.authorize.domain.PaymentTransaction;
import com.bridgeline.authorize.domain.Vendor;
import com.bridgeline.authorize.dto.PaymentAuthorizationRequest;
import com.bridgeline.authorize.enums.AuthorizationStatusEnum;
import com.bridgeline.authorize.repository.CardDetailsRepository;
import com.bridgeline.authorize.repository.CardTypeRepository;
import com.bridgeline.authorize.repository.PaymentTransactionRepository;
import com.bridgeline.authorize.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class PaymentAuthorizationService {
    @Autowired
    CardDetailsRepository cardDetailsRepository;
    @Autowired
    CardTypeRepository cardTypeRepository;
    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    VendorRepository vendorRepository;

    public void authorizeCreditCard(PaymentAuthorizationRequest paymentAuthorizationRequest) throws UnknownHostException {
        com.bridgeline.authorize.dto.CardDetails cardDetails1 = paymentAuthorizationRequest.getCardDetails();
        CardDetails cardDetails = CardDetails.builder()
                        .cardNumber(cardDetails1.getCardNumber())
                        .expirationDate(cardDetails1.getExpirationDate())
                         .cardholderName(cardDetails1.getCardholderName())
                          .cvv(cardDetails1.getCvv())
                            .build();
        CardDetails savedCardDetails = cardDetailsRepository.save(cardDetails);

        CardType cardType = CardType.builder()
                .cardId(savedCardDetails.getCardId())
                .cardName(cardDetails1.getCardName())
                .build();
        CardType saveCardType = cardTypeRepository.save(cardType);

        Vendor vendor = Vendor.builder()
                .vendorName("summit")
                .vendorLocation("US")
                .build();

        Vendor saveVendor = vendorRepository.save(vendor);

        PaymentTransaction paymentTransaction = PaymentTransaction.builder()
                .amount(paymentAuthorizationRequest.getAmount())
                .currency(paymentAuthorizationRequest.getCurrency())
                .createdTime(LocalDateTime.now())
                .status(AuthorizationStatusEnum.APPROVED.name())
                .authorizationCode(AuthorizationStatusEnum.APPROVED.getCode())
                .ipAddress(InetAddress.getLocalHost().getHostAddress())
                .userAgent(null)
                .customerAccountId("null")
                .paymentMethod(paymentAuthorizationRequest.getPaymentMethod())
                .cardDetails(savedCardDetails)
                .vendorId(saveVendor.getVendorId())
                .build();
        paymentTransactionRepository.save(paymentTransaction);
    }

}
