package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerProfileUpdateDto {

    // ===== USER =====
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    // ===== FARMER =====
    @NotBlank(message = "Farm type is required")
    private String farmType;

    @NotNull(message = "Land area is required")
    @Positive(message = "Land area must be greater than 0")
    private Double landArea;

   
}
