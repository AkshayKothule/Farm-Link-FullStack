package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerProfileUpdateDto {

    // -------- USER (allowed identity fields) --------
    @NotBlank(message = "FirstName Should Not Blank")
    private String firstName;

    @NotBlank(message = "LastName Should Not Blank")
    private String lastName;

    @NotBlank(message = "Phone Number Should Not Blank")
    private String phoneNumber;

    // -------- FARMER (domain fields) --------
    @NotBlank()
    private String farmType;

    @NotBlank()
    private Double landArea;
}
