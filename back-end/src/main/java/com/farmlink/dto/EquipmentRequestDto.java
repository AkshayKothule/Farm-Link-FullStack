package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequestDto {

    @NotBlank(message="Equipment Name Required")
    private String name;

    @NotBlank(message = "Equipment Category Required")
    private String category;   // Tractor / Harvester / Pump

    @NotNull(message="Rent Needed")
    private Double rentPerDay;

    private Boolean available;
    private String description;
    private String imageUrl;
}
