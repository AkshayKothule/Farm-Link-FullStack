package com.farmlink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.farmlink.dto.PaymentRequestDto;
import com.farmlink.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> pay(
            @RequestBody PaymentRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        paymentService.makePayment(dto, userDetails.getUsername());
        return ResponseEntity.ok("Payment successful");
    }
}
