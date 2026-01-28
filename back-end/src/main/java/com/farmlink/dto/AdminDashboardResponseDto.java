package com.farmlink.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminDashboardResponseDto {

    // USERS
    private long totalUsers;
    private long totalFarmers;
    private long totalOwners;
    private long pendingOwners;

    // EQUIPMENTS
    private long totalEquipments;
    private long availableEquipments;

    // RENTALS
    private long totalRentals;
    private long approvedRentals;
    private long rejectedRentals;

    // REVIEWS
    private long totalReviews;
    private double averageRatingOverall;
}
