package com.farmlink.services;
import java.util.List;

import java.util.List;

import com.farmlink.dto.AdminDashboardResponseDto;
import com.farmlink.dto.AdminFarmerResponseDto;
import com.farmlink.dto.AdminOwnerResponseDto;
import com.farmlink.dto.AdminReviewResponseDto;

public interface AdminService {

    // existing
    void verifyOwner(Long ownerId);
    void deleteReview(Long reviewId);
    public List<AdminOwnerResponseDto> getAllOwners() ;
    public AdminDashboardResponseDto getDashboardData();


    // 🔹 ADD THIS
    List<AdminFarmerResponseDto> getAllFarmers();
    
    List<AdminReviewResponseDto> getAllReviews();

}
