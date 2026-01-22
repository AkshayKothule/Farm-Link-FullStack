package com.farmlink.dto;


import java.time.LocalDate;

import com.farmlink.entities.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRentalResponseDto {

    private Long rentalId;
    private String farmerName;
    private String equipmentName;
    private String equipmentCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
}
