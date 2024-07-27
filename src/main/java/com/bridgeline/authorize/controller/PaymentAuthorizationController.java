package com.bridgeline.authorize.controller;

import com.bridgeline.authorize.dto.CardDetails;
import com.bridgeline.authorize.dto.CardDetailsWithAdditionalData;
import com.bridgeline.authorize.dto.PaymentAuthorizationRequest;
import com.bridgeline.authorize.service.PaymentAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentAuthorizationController {
    @Autowired
    PaymentAuthorizationService paymentAuthorizationService;
    @PostMapping("/authorize")
    public void authorizeCreditCard(@RequestBody PaymentAuthorizationRequest paymentAuthorizationRequest) {
        paymentAuthorizationService.authorizeCreditCard(paymentAuthorizationRequest);
    }
    @GetMapping("/{amount}")
    public List<CardDetails> findByAmount(@PathVariable("amount") BigDecimal amount){
        return paymentAuthorizationService.findByAmount(amount);
    }
    @GetMapping("/currency/{currency}")
    public List<CardDetailsWithAdditionalData> findByCurrency(@PathVariable("currency") String currency){
        return paymentAuthorizationService.findByCurrency(currency);
    }
    @GetMapping
    public List<CardDetailsWithAdditionalData> findByAmountOrCurrency
            (@RequestParam(required = false , name = "byAmount") BigDecimal amount, @RequestParam(required = false ,name = "byCurrency") String currency){
        return paymentAuthorizationService.findByAmountOrCurrency(amount, currency);
    }

}
