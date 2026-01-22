package com.farmlink.dto;
import java.time.LocalDate;

import com.farmlink.entities.RentalStatus;

public class FarmerRentalResponseDto {

    private Long rentalId;
    private String equipmentName;
    private String equipmentCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
}
