package com.farmlink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.OwnerProfileRequestDto;
import com.farmlink.services.OwnerService;




import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping("/profile")
    public ResponseEntity<String> createProfile(
            @Valid @RequestBody OwnerProfileRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        ownerService.createOwnerProfile(dto, userDetails.getUsername());
        return ResponseEntity.ok("Owner profile ready");
    }

}
