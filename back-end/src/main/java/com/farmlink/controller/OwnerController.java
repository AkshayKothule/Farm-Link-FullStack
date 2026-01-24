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

import com.farmlink.dto.OwnerProfileRequestDto;
import com.farmlink.dto.OwnerProfileResponseDto;
import com.farmlink.dto.OwnerProfileUpdateDto;
import com.farmlink.services.OwnerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    // =========================
    // CREATE OWNER PROFILE
    // =========================
    @PostMapping("/profile")
    public ResponseEntity<String> createProfile(
            @Valid @RequestBody OwnerProfileRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        ownerService.createOwnerProfile(dto, userDetails.getUsername());
        return ResponseEntity.ok("Owner profile ready");
    }
    
    
    
    // =========================
    // READ OWNER PROFILE
    // =========================
    @GetMapping("/profile")
    public ResponseEntity<OwnerProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                ownerService.getMyProfile(
                        userDetails.getUsername()
                )
        );
    }

    // =========================
    // UPDATE OWNER PROFILE
    // =========================
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @Valid @RequestBody OwnerProfileUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        ownerService.updateOwnerProfile(
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Owner profile updated successfully");
    }

}
