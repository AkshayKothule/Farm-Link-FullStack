package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerProfileResponseDto {

    // ===== USER INFO =====
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    // ===== FARMER INFO =====
    private String farmType;
    private Double landArea;
    private Integer experience;
}
