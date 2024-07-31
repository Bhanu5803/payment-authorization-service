package com.bridgeline.authorize.service;


import com.bridgeline.authorize.domain.CardDetails;
import com.bridgeline.authorize.domain.CardType;
import com.bridgeline.authorize.domain.PaymentTransaction;
import com.bridgeline.authorize.domain.Vendor;
import com.bridgeline.authorize.dto.CardDetailsWithAdditionalData;
import com.bridgeline.authorize.dto.PaymentAuthorizationRequest;
import com.bridgeline.authorize.enums.AuthorizationStatusEnum;
import com.bridgeline.authorize.repository.CardDetailsRepository;
import com.bridgeline.authorize.repository.CardTypeRepository;
import com.bridgeline.authorize.repository.PaymentTransactionRepository;
import com.bridgeline.authorize.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        Vendor vendor = Vendor.builder()
                .vendorName("Summit")
                .vendorLocation("US")
                .build();
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

    public List<com.bridgeline.authorize.dto.CardDetails> findByAmount(BigDecimal amount) {

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll();
        List<com.bridgeline.authorize.dto.CardDetails> cardDetailsList = new ArrayList<>();

        List<PaymentTransaction> paymentTransactions = paymentTransactionList.stream()
                .filter(p -> p.getAmount().compareTo(amount) > 0).toList();

        paymentTransactions.forEach(p-> {
            CardDetails cardDetails = p.getCardDetails();
            com.bridgeline.authorize.dto.CardDetails cardDetails1 = new com.bridgeline.authorize.dto.CardDetails();
            cardDetails1.setCardholderName(cardDetails.getCardholderName());
            cardDetails1.setCardNumber(cardDetails.getCardNumber());
            cardDetails1.setCvv(cardDetails.getCvv());
            cardDetails1.setExpirationDate(cardDetails.getExpirationDate());

            Integer cardId = cardDetails.getCardId();
            CardType cardType = cardTypeRepository.findByCardId(cardId);
            cardDetails1.setCardName(cardType.getCardName());
            cardDetailsList.add(cardDetails1);
            
        });
        return cardDetailsList;
    }

    public List<CardDetailsWithAdditionalData> findByCurrency(String currency) {
        List<PaymentTransaction> byCurrency = paymentTransactionRepository.findByCurrency(currency);
        List<PaymentTransaction> paymentTransactionListUSD = byCurrency.stream().filter(p -> p.getCurrency().equalsIgnoreCase("USD"))
                .toList();
        List<CardDetailsWithAdditionalData> cardDetailsWithAdditionalData  = new ArrayList<>();
        paymentTransactionListUSD.forEach(p->
        {
            BigDecimal amount = p.getAmount();
            String currency1 = p.getCurrency();
            CardDetails cardDetails = p.getCardDetails();
            com.bridgeline.authorize.dto.CardDetails cardDetails1 = new com.bridgeline.authorize.dto.CardDetails();
            cardDetails1.setCardNumber(cardDetails.getCardNumber());
            cardDetails1.setCvv(cardDetails.getCvv());
            cardDetails1.setCardholderName(cardDetails.getCardholderName());
            cardDetails1.setExpirationDate(cardDetails.getExpirationDate());
            Integer cardId = cardDetails.getCardId();
            CardType byCardId = cardTypeRepository.findByCardId(cardId);
            cardDetails1.setCardName(byCardId.getCardName());

            CardDetailsWithAdditionalData cardDetailsWithAmount = new CardDetailsWithAdditionalData();
            cardDetailsWithAmount.setAmount(amount);
            cardDetailsWithAmount.setCurrency(currency1);
            cardDetailsWithAmount.setCardDetails(cardDetails1);
            cardDetailsWithAdditionalData.add(cardDetailsWithAmount);
        });
        return cardDetailsWithAdditionalData;
    }


    public List<CardDetailsWithAdditionalData> findByAmountOrCurrency(BigDecimal amount, String currency) {
        List<PaymentTransaction> byAmountOrCurrency = paymentTransactionRepository.findAll();
        List<PaymentTransaction> paymentTransactionListAmountUsd = byAmountOrCurrency.stream()
                .filter(a -> amount==null || a.getAmount().compareTo(amount) > 0)
                .filter(a -> currency==null || a.getCurrency().equalsIgnoreCase(currency))
                .toList();
        List<CardDetailsWithAdditionalData> cardDetailsWithAdditionalDataList = new ArrayList<>();
        paymentTransactionListAmountUsd.forEach(p-> {
            BigDecimal amount1 = p.getAmount();
            String currency1 = p.getCurrency();
            CardDetails cardDetails = p.getCardDetails();
            CardDetailsWithAdditionalData cardDetailsWithAdditionalData = new CardDetailsWithAdditionalData();
            cardDetailsWithAdditionalData.setAmount(amount1);
            cardDetailsWithAdditionalData.setCurrency(currency1);
            com.bridgeline.authorize.dto.CardDetails cardDetailsDto = convertDomainToDTO(cardDetails);
            cardDetailsWithAdditionalData.setCardDetails(cardDetailsDto);
            cardDetailsWithAdditionalDataList.add(cardDetailsWithAdditionalData);

        });
        return cardDetailsWithAdditionalDataList;
    }
    private com.bridgeline.authorize.dto.CardDetails convertDomainToDTO(CardDetails cardDetails) {
        com.bridgeline.authorize.dto.CardDetails cardDetails1 = new com.bridgeline.authorize.dto.CardDetails();
        cardDetails1.setCardNumber(cardDetails.getCardNumber());
        cardDetails1.setCvv(cardDetails.getCvv());
        cardDetails1.setCardholderName(cardDetails.getCardholderName());
        cardDetails1.setExpirationDate(cardDetails.getExpirationDate());
        CardType byCardId = cardTypeRepository.findByCardId(cardDetails.getCardId());
        cardDetails1.setCardName(byCardId.getCardName());
        return cardDetails1;
    }
}
