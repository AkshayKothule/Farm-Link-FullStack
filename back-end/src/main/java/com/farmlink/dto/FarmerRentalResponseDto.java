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
public class FarmerRentalResponseDto {

    private Long rentalId;
    private String equipmentName;
    private String equipmentCategory;
    private String ownerName;          // ✅ ADD THIS
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
}

