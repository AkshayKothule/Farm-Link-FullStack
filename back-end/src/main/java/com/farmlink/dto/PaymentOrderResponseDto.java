package com.farmlink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentOrderResponseDto {
    private Long paymentId;
    private Double amount;
}
