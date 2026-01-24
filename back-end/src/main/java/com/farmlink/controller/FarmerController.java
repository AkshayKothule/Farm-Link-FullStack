package com.farmlink.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.dto.FarmerProfileResponseDto;
import com.farmlink.dto.FarmerProfileUpdateDto;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.User;
import com.farmlink.services.FarmerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    // ✅ GET PROFILE (auto-create if not exists)
    @GetMapping("/profile")
    public ResponseEntity<FarmerProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                farmerService.getMyProfile(userDetails.getUsername())
        );
    }

    // ✅ CREATE PROFILE (manual – optional)
    @PostMapping("/profile")
    public ResponseEntity<String> createProfile(
            @Valid @RequestBody FarmerProfileRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        farmerService.createFarmerProfile(dto, userDetails.getUsername());
        return ResponseEntity.ok("Farmer profile created successfully");
    }

    // ✅ UPDATE PROFILE
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody FarmerProfileUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        farmerService.updateFarmerProfile(dto, userDetails.getUsername());
        return ResponseEntity.ok("Farmer profile updated successfully");
    }
}
