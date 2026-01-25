package com.farmlink.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookedDateDto {
    private LocalDate startDate;
    private LocalDate endDate;
}

