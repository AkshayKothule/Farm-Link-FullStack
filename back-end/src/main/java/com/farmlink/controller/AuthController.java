package com.farmlink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.LoginRequestDto;
import com.farmlink.dto.LoginResponseDto;
import com.farmlink.dto.UserRegistrationDto;
import com.farmlink.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    // 🔓 PUBLIC REGISTRATION ENDPOINT ---> Registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegistrationDto dto) {

        authService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
    
    
    
    // 🔓 PUBLIC REGISTRATION ENDPOINT ---> Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

}
