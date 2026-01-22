package com.farmlink.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerProfileUpdateDto {

    // ---- USER FIELDS ----
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    // ---- OWNER FIELD ----
    @NotBlank
    private String businessName;
}
