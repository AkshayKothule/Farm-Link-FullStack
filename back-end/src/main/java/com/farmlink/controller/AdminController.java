package com.farmlink.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.AdminDashboardResponseDto;
import com.farmlink.dto.AdminFarmerResponseDto;
import com.farmlink.dto.AdminOwnerResponseDto;
import com.farmlink.dto.AdminReviewResponseDto;
import com.farmlink.services.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 📊 DASHBOARD
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponseDto> getDashboard() {
        return ResponseEntity.ok(
            adminService.getDashboardData()
        );
    }

    // ✅ VERIFY OWNER
    @PutMapping("/owners/{ownerId}/verify")
    public ResponseEntity<String> verifyOwner(
            @PathVariable("ownerId") Long ownerId) {

        adminService.verifyOwner(ownerId);
        return ResponseEntity.ok("Owner verified successfully");
    }

    // 🗑 DELETE REVIEW
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") Long reviewId) {

        adminService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
    
    
    @GetMapping("/owners")
    public ResponseEntity<List<AdminOwnerResponseDto>> getAllOwners() {
        return ResponseEntity.ok(adminService.getAllOwners());
    }
    
    @GetMapping("/farmers")
    public ResponseEntity<List<AdminFarmerResponseDto>> getAllFarmers() {
        return ResponseEntity.ok(adminService.getAllFarmers());
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<AdminReviewResponseDto>> getAllReviews() {
        return ResponseEntity.ok(
            adminService.getAllReviews()
        );
    }

}
