package com.farmlink.dto;

import java.time.LocalDateTime;

import com.farmlink.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentHistoryDto {

    private String equipmentName;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;
}
