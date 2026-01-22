package com.farmlink.dto;
import java.time.LocalDate;

import com.farmlink.entities.RentalStatus;

public class OwnerRentalResponseDto {

    private Long rentalId;
    private String farmerName;
    private String equipmentName;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
}
