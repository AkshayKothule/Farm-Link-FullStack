package com.farmlink.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentResponseDto {

    private Long id;
    private String name;
    private String category;
    private Double rentPerDay;
    private Boolean available;
    private String description;
    private List<String> imageUrls; // 👈 Cloudinary URLs
}
