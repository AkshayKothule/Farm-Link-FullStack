package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerProfileResponseDto {

    private String farmType;
    private Double landArea;
    private Integer experience;
}
