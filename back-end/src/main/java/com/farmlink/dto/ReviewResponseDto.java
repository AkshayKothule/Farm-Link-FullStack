package com.farmlink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponseDto {

    private Integer rating;
    private String comment;
    private String farmerName;
}
