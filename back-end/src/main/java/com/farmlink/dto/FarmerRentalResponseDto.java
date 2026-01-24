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

    private RentalStatus status;

    private Double totalAmount; // NULL until APPROVED
}
