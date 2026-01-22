package com.farmlink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResponseDto {

    private String orderId;   // Razorpay order_id
    private String keyId;     // Razorpay public key
    private Double amount;    // Amount in INR
}
