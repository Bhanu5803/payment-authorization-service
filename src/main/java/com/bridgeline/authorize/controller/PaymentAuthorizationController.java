package com.bridgeline.authorize.controller;

import com.bridgeline.authorize.dto.PaymentAuthorizationRequest;
import com.bridgeline.authorize.service.PaymentAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentAuthorizationController {
    @Autowired
    PaymentAuthorizationService paymentAuthorizationService;
    @PostMapping("/authorize")
    public void authorizeCreditCard(@RequestBody PaymentAuthorizationRequest paymentAuthorizationRequest) {
        paymentAuthorizationService.authorizeCreditCard(paymentAuthorizationRequest);
    }

}
