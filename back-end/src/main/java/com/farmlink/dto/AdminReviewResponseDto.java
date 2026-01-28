
package com.farmlink.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AdminReviewResponseDto {

    private Long id;
    private String userName;
    private String comment;
    private Integer rating;
}
