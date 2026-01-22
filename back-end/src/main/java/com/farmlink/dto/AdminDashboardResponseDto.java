package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponseDto {

    // USERS
    private Long totalUsers;
    private Long totalFarmers;
    private Long totalOwners;

    // EQUIPMENT
    private Long totalEquipments;
    private Long availableEquipments;

    // RENTALS
    private Long totalRentals;
    private Long approvedRentals;
    private Long rejectedRentals;

    // REVIEWS
    private Long totalReviews;
    private Double averageRatingOverall;
}
