package com.farmlink.services;

import com.farmlink.dto.PaymentOrderResponseDto;
import com.farmlink.dto.PaymentVerifyRequestDto;

public interface PaymentService {

    // STEP 1: Create Razorpay Order
    PaymentOrderResponseDto createOrder(Long rentalId, String email);

    // STEP 2: Verify Razorpay Payment
    void verifyPayment(PaymentVerifyRequestDto dto);
}
