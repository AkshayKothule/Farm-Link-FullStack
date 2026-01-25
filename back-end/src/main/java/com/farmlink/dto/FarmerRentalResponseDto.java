package com.farmlink.dto;

import java.time.LocalDate;

import com.farmlink.entities.RentalStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerRentalResponseDto {

    private Long rentalId;

    private String equipmentName;
    private String equipmentCategory;

    private String ownerName;

    private LocalDate startDate;
    private LocalDate endDate;

    private RentalStatus status;   // PENDING / APPROVED / REJECTED

    private Double totalAmount;    // approved नंतर

    // 🔥 ADD THIS
    private String paymentStatus;  // CREATED / SUCCESS / FAILED / null
}
