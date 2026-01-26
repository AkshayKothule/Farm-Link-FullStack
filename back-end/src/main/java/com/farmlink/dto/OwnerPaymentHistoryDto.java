

package com.farmlink.dto;

import java.time.LocalDateTime;

import com.farmlink.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OwnerPaymentHistoryDto {

    private String equipmentName;
    private String farmerName;
    private Double amount;
    private PaymentStatus status;
    private LocalDateTime paidAt;
}
