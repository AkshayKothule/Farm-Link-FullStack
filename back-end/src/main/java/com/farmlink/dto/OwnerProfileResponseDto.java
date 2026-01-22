package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerProfileResponseDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String businessName;
    private Boolean verified;
}

