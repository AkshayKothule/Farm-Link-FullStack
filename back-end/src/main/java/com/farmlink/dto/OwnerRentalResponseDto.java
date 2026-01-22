package com.farmlink.dto;

import java.time.LocalDate;

import com.farmlink.entities.RentalStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerRentalResponseDto {

    private Long rentalId;

    private String farmerName;

    private String equipmentName;
    private String equipmentCategory;

    private LocalDate startDate;
    private LocalDate endDate;

    private RentalStatus status;

    private Double totalAmount; // Visible after approval
}
