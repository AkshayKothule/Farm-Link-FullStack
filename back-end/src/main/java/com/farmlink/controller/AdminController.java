package com.farmlink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.services.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // VERIFY OWNER
    @PutMapping("/owners/{ownerId}/verify")
    public ResponseEntity<String> verifyOwner(
            @PathVariable Long ownerId) {

        adminService.verifyOwner(ownerId);
        return ResponseEntity.ok("Owner verified successfully");
    }

    // DELETE REVIEW
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId) {

        adminService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
