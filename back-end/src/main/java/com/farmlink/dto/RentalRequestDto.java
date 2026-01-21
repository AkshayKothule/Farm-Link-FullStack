package com.farmlink.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalRequestDto {

    @NotNull
    private Long equipmentId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}

