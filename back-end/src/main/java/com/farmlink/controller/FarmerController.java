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

import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.dto.FarmerProfileResponseDto;
import com.farmlink.dto.FarmerProfileUpdateDto;
import com.farmlink.services.FarmerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @PostMapping("/profile")
    public ResponseEntity<String> createOrGetProfile(
            @Valid @RequestBody FarmerProfileRequestDto farmerProfileRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        farmerService.createFarmerProfile(
                farmerProfileRequestDto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Farmer profile created successfully");
    }

    
    @GetMapping("/profile")
    public ResponseEntity<FarmerProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                farmerService.getMyProfile(
                        userDetails.getUsername()
                )
        );
    }

    
    
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody FarmerProfileUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        farmerService.updateFarmerProfile(
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Farmer profile updated successfully");
    }


}
