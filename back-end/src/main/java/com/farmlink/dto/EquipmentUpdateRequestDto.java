package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentUpdateRequestDto {

    @NotBlank(message="Equipment Name is Required")
    private String name;

    @NotBlank(message="Category Required")
    private String category;

    @NotNull(message="Rate Required")
    private Double rentPerDay;

    private Boolean available;
    private String description;
    private String imageUrl;
}
