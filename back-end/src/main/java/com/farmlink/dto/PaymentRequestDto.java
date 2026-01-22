package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long rentalRequestId;
    private String paymentMode; // UPI / CARD / CASH
}
