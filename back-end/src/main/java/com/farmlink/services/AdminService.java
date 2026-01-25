package com.farmlink.services;

import com.farmlink.dto.AdminDashboardResponseDto;

public interface AdminService {

    void verifyOwner(Long ownerId);

    void deleteReview(Long reviewId);
    
//    AdminDashboardResponseDto getDashboardData();

}

