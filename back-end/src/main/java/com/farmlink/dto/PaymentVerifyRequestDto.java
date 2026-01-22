package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifyRequestDto {

    @NotBlank
    private String paymentId;     // razorpay_payment_id

    @NotBlank
    private String orderId;       // razorpay_order_id

    @NotBlank
    private String signature;     // razorpay_signature
}
