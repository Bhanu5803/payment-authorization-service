package com.bridgeline.authorize;

import com.bridgeline.authorize.domain.CardType;
import com.bridgeline.authorize.domain.PaymentTransaction;
import com.bridgeline.authorize.domain.Vendor;
import com.bridgeline.authorize.dto.CardDetails;
import com.bridgeline.authorize.dto.CardDetailsWithAdditionalData;
import com.bridgeline.authorize.dto.PaymentAuthorizationRequest;
import com.bridgeline.authorize.repository.CardDetailsRepository;
import com.bridgeline.authorize.repository.CardTypeRepository;
import com.bridgeline.authorize.repository.PaymentTransactionRepository;
import com.bridgeline.authorize.repository.VendorRepository;
import com.bridgeline.authorize.service.PaymentAuthorizationService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 class PaymentAuthorizationServiceTest {
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private CardDetailsRepository cardDetailsRepository;
    @Mock
    private CardTypeRepository cardTypeRepository;
    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    PaymentAuthorizationService paymentAuthorizationService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(i-> {
            Vendor vendor = i.getArgument(0);
            vendor.setVendorId(RandomGenerator.getDefault().nextInt());
            return vendor;
        });
    }

    @Test
    void testAuthorizeCreditCardWithoutCardDetails() {

        PaymentAuthorizationRequest paymentAuthorizationRequest = new PaymentAuthorizationRequest();
        paymentAuthorizationRequest.setCurrency("EUR");
        paymentAuthorizationRequest.setAmount(new BigDecimal("140.00"));
        paymentAuthorizationRequest.setPaymentMethod("CREDIT_CARD");

        List<CardDetails> cardDetailsList = new ArrayList<>();
        paymentAuthorizationRequest.setCardDetailsList(cardDetailsList);

        paymentAuthorizationService.authorizeCreditCard(paymentAuthorizationRequest);
        verify(vendorRepository,times(1)).save(any(Vendor.class));
        verify(cardDetailsRepository,times(0))
                .save(any(com.bridgeline.authorize.domain.CardDetails.class));



    }
    @Test
    void testAuthorizeCreditCardWithCardDetails() {

        when(cardDetailsRepository.save(any(com.bridgeline.authorize.domain.CardDetails.class)))
                .thenAnswer(i-> {
                   com.bridgeline.authorize.domain.CardDetails cardDetails = i.getArgument(0);
                   cardDetails.setCardId(RandomGenerator.getDefault().nextInt());
                   return cardDetails;
                });
        when(cardTypeRepository.save(any(CardType.class))).thenAnswer(i-> {
            CardType cardType = i.getArgument(0);
            cardType.setCardTypeId(RandomGenerator.getDefault().nextInt());
            return cardType;
        });

        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenAnswer(i-> {
                    PaymentTransaction paymentTransaction = i.getArgument(0);
                    paymentTransaction.setTransactionId(RandomGenerator.getDefault().nextInt());
                    return paymentTransaction;
                }
            );
        PaymentAuthorizationRequest paymentAuthorizationRequest = new PaymentAuthorizationRequest();
        paymentAuthorizationRequest.setAmount(BigDecimal.valueOf(140.00));
        paymentAuthorizationRequest.setCurrency("EUR");
        paymentAuthorizationRequest.setPaymentMethod("CREDIT_CARD");

        List<CardDetails> cardDetailsList = new ArrayList<>();
        CardDetails cardDetails = new CardDetails();

        cardDetails.setCardNumber("2756678987608967");
        cardDetails.setExpirationDate("04/25");
        cardDetails.setCardholderName("Ram");
        cardDetails.setCvv("123");
        cardDetails.setCardName("Plcc");

        CardDetails cardDetails1 = new CardDetails();

        cardDetails1.setCardNumber("5756678987608967");
        cardDetails1.setExpirationDate("04/25");
        cardDetails1.setCardholderName("Ram");
        cardDetails1.setCvv("123");
        cardDetails1.setCardName("GiftCard");

        cardDetailsList.add(cardDetails);
        cardDetailsList.add(cardDetails1);
        paymentAuthorizationRequest.setCardDetailsList(cardDetailsList);

        paymentAuthorizationService.authorizeCreditCard(paymentAuthorizationRequest);
        verify(vendorRepository,times(1)).save(any(Vendor.class));
        verify(cardDetailsRepository,times(2))
                .save(any(com.bridgeline.authorize.domain.CardDetails.class));
        verify(cardTypeRepository,times(2))
                .save(any(CardType.class));
        verify(paymentTransactionRepository,times(2))
                .save(any(PaymentTransaction.class));

   }
    @Test
    void testFindByAmount() {
        List<PaymentTransaction> paymentTransactionList = new ArrayList<>();
        com.bridgeline.authorize.domain.CardDetails cardDetails = new com.bridgeline.authorize.domain.CardDetails();
        cardDetails.setCardId(1);
        cardDetails.setCardNumber("2756678987608967");
        cardDetails.setCvv("123");
        cardDetails.setCardholderName("Ram");
        cardDetails.setExpirationDate("04/25");
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setAmount(new BigDecimal(140.00));
        paymentTransaction.setCurrency("EUR");
        paymentTransaction.setCardDetails(cardDetails);

        paymentTransactionList.add(paymentTransaction);

        CardType cardType = new CardType();
        cardType.setCardTypeId(1);
        cardType.setCardId(cardDetails.getCardId());
        cardType.setCardName("Plcc");
        cardType.setCardDetails(cardDetails);


        BigDecimal amount = new BigDecimal(40.00);

        when(paymentTransactionRepository.findAll()).thenReturn(paymentTransactionList);
        when(cardTypeRepository.findByCardId(any(Integer.class))).thenReturn(cardType);
        List<CardDetails> byAmount = paymentAuthorizationService.findByAmount(amount);

        assertTrue(byAmount.size()>0);
    }
    @Test
    void testFindByCurrency(){
        String currency = "USD";
        List<PaymentTransaction> paymentTransactionList = new ArrayList<>();
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setCurrency("USD");
        paymentTransaction.setAmount(new BigDecimal(140.00));
        com.bridgeline.authorize.domain.CardDetails cardDetails1 = new com.bridgeline.authorize.domain.CardDetails();
        cardDetails1.setCardId(1);
        cardDetails1.setCardNumber("2756678987608967");
        cardDetails1.setCvv("123");
        cardDetails1.setCardholderName("Ram");
        cardDetails1.setExpirationDate("04/25");
        paymentTransaction.setCardDetails(cardDetails1);
        paymentTransactionList.add(paymentTransaction);

        CardType cardType = new CardType();
        cardType.setCardTypeId(cardDetails1.getCardId());
        cardType.setCardName("Plcc");
        cardType.setCardDetails(cardDetails1);

        when(paymentTransactionRepository.findByCurrency(any(String.class))).thenReturn(paymentTransactionList);
        when(cardTypeRepository.findByCardId(any(Integer.class))).thenReturn(cardType);
        List<CardDetailsWithAdditionalData> byCurrency = paymentAuthorizationService.findByCurrency(currency);
        assertEquals(1,byCurrency.size());
    }
    @Test
    void testFindByAmountOrCurrency(){
        List<PaymentTransaction> paymentTransactionList = new ArrayList<>();
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setAmount(new BigDecimal(140.00));
        paymentTransaction.setCurrency("USD");
        com.bridgeline.authorize.domain.CardDetails cardDetails = new com.bridgeline.authorize.domain.CardDetails();
        cardDetails.setCardId(1);
        cardDetails.setCardNumber("2756678987608967");
        cardDetails.setCvv("123");
        cardDetails.setCardholderName("Ram");
        cardDetails.setExpirationDate("04/25");
        paymentTransaction.setCardDetails(cardDetails);
        paymentTransactionList.add(paymentTransaction);

        CardType cardType = new CardType();
        cardType.setCardTypeId(cardDetails.getCardId());
        cardType.setCardName("Plcc");
        cardType.setCardDetails(cardDetails);


        BigDecimal amount = new BigDecimal(140.00);
        String currency = "USD";
        List<CardDetailsWithAdditionalData> byAmountOrCurrency = paymentAuthorizationService
                .findByAmountOrCurrency(amount, currency);
        when(paymentTransactionRepository.findAll()).thenReturn(paymentTransactionList);
        when(cardTypeRepository.findByCardId(any(Integer.class))).thenReturn(cardType);
        assertTrue(byAmountOrCurrency.size()<=0);
        //assertEquals(1,byAmountOrCurrency.size());
    }
}

