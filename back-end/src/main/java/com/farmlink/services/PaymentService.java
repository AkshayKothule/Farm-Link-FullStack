package com.farmlink.services;

import java.util.List;

import com.farmlink.dto.PaymentHistoryDto;
import com.farmlink.dto.PaymentOrderResponseDto;
import com.farmlink.dto.PaymentVerifyRequestDto;
import com.razorpay.RazorpayException;

public interface PaymentService {

    // STEP 1: Create Razorpay Order
	public PaymentOrderResponseDto createOrder(
	        Long rentalId, String email) throws RazorpayException;
    // STEP 2: Verify Razorpay Payment
    void verifyPayment(PaymentVerifyRequestDto dto);
    
    List<PaymentHistoryDto> getFarmerPayments(String email);

}
