package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDto {

    @NotBlank(message = "Address line is required")
    @Size(max = 255, message = "Address line too long")
    private String addressLine;

    @NotBlank(message = "Village is required")
    @Size(max = 100)
    private String village;

    @NotBlank(message = "Taluka is required")
    @Size(max = 100)
    private String taluka;

    @NotBlank(message = "District is required")
    @Size(max = 100)
    private String district;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "Pincode is required")
    @Size(min = 5, max = 10, message = "Invalid pincode")
    private String pincode;
}
