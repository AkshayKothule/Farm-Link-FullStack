package com.farmlink.dto;



import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentBrowseResponseDto {

    private Long id;
    private String name;
    private String category;
    private Double rentPerDay;
    private String description;
    private List<String> imageUrls;

    // Owner info (read-only)
    private String ownerBusinessName;
}
