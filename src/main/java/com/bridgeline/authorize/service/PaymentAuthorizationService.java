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
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentAuthorizationService {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CardDetailsRepository cardDetailsRepository;
    @Autowired
    private CardTypeRepository cardTypeRepository;
    @Autowired
   private PaymentTransactionRepository paymentTransactionRepository;


    public void authorizeCreditCard(PaymentAuthorizationRequest paymentAuthorizationRequest) {
        Vendor vendor = new Vendor();
        vendor.setVendorName("Summit");
        vendor.setVendorLocation("US");
        Vendor saveVendor = vendorRepository.save(vendor);
        log.info("vendor saved successfully : {}", saveVendor);

        List<com.bridgeline.authorize.dto.CardDetails> cardDetailsList = paymentAuthorizationRequest.getCardDetailsList();

        cardDetailsList.forEach(c-> {
            CardDetails cardDetails = new CardDetails();
            cardDetails.setCardNumber(c.getCardNumber());
            cardDetails.setCvv(c.getCvv());
            cardDetails.setCardholderName(c.getCardholderName());
            cardDetails.setExpirationDate(c.getExpirationDate());
            CardDetails saveCardDetails = cardDetailsRepository.save(cardDetails);

            CardType cardType = new CardType();
            cardType.setCardName(c.getCardName());
            cardType.setCardId(saveCardDetails.getCardId());
            cardType.setCardDetails(saveCardDetails);

            CardType saveCardType = cardTypeRepository.save(cardType);
            log.info("Card Type saved successfully : {}", saveCardType);

            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setAmount(paymentAuthorizationRequest.getAmount());
            paymentTransaction.setCurrency(paymentAuthorizationRequest.getCurrency());
            paymentTransaction.setCreatedTime(LocalDateTime.now());
            paymentTransaction.setStatus(AuthorizationStatusEnum.APPROVED.name());
            paymentTransaction.setAuthorizationCode(AuthorizationStatusEnum.APPROVED.getCode());
            paymentTransaction.setPaymentMethod(paymentAuthorizationRequest.getPaymentMethod());
            paymentTransaction.setIpAddress(null);
            paymentTransaction.setCustomerAccountId(null);
            paymentTransaction.setVendorId(saveVendor.getVendorId());
            paymentTransaction.setCardDetails(saveCardDetails);
            PaymentTransaction savePaymentTransaction = paymentTransactionRepository.save(paymentTransaction);
            log.info("Payment Transaction saved successfully : {}", savePaymentTransaction);
        });


    }
}
