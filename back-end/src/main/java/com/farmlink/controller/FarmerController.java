package com.farmlink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.services.FarmerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @PostMapping("/profile")
    public ResponseEntity<String> createOrGetProfile( @RequestBody FarmerProfileRequestDto farmerProfileRequestDto ,
            @AuthenticationPrincipal UserDetails userDetails) {

        farmerService.createFarmerProfile(farmerProfileRequestDto ,userDetails.getUsername());

        return ResponseEntity.ok("Farmer profile ready");
    }
}
