package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminOwnerResponseDto {

    private Long id;
    private String userName;
    private String businessName;
    private boolean verified;
}
