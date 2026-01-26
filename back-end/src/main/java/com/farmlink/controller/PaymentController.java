package com.farmlink.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.farmlink.dto.OwnerPaymentHistoryDto;
import com.farmlink.dto.PaymentHistoryDto;
import com.farmlink.dto.PaymentOrderResponseDto;
import com.farmlink.dto.PaymentVerifyRequestDto;
import com.farmlink.services.PaymentService;
import com.razorpay.RazorpayException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // STEP 1: Create Razorpay Order
    @PostMapping("/create/{rentalId}")
    public ResponseEntity<PaymentOrderResponseDto> createOrder(
            @PathVariable("rentalId") Long rentalId,
            @AuthenticationPrincipal UserDetails userDetails) throws RazorpayException {

        return ResponseEntity.ok(
                paymentService.createOrder(
                        rentalId,
                        userDetails.getUsername())
        );
    }

    // STEP 2: Verify Razorpay Payment
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequestDto dto) {

        paymentService.verifyPayment(dto);
        return ResponseEntity.ok("Payment verified successfully");
    }
    
    
    @GetMapping("/farmer")
    public ResponseEntity<List<PaymentHistoryDto>> paymentHistory(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            paymentService.getFarmerPayments(user.getUsername())
        );
    }
    @GetMapping("/owner")
    public ResponseEntity<List<OwnerPaymentHistoryDto>> ownerPayments(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
            paymentService.getOwnerPayments(userDetails.getUsername())
        );
    }


}
