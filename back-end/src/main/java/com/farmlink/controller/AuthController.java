package com.farmlink.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.farmlink.dto.ChangePasswordDto;
import com.farmlink.dto.ForgotPasswordDto;
import com.farmlink.dto.LoginRequestDto;
import com.farmlink.dto.LoginResponseDto;
import com.farmlink.dto.ResetPasswordDto;
import com.farmlink.dto.UserRegistrationDto;
import com.farmlink.entities.User;
import com.farmlink.repository.UserRepository;
import com.farmlink.services.AuthService;
import com.farmlink.services.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

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
    
    
@PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(
        @Valid @RequestBody ForgotPasswordDto dto) {

    User user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() ->
            new RuntimeException("User not found"));

    String token = UUID.randomUUID().toString();

    user.setResetToken(token);
    user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
    userRepository.save(user);

    emailService.sendResetMail(user.getEmail(), token);

    return ResponseEntity.ok("Reset email sent");
}


@PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(
        @Valid @RequestBody ResetPasswordDto dto) {

    User user = userRepository.findByResetToken(dto.getToken())
        .orElseThrow(() ->
            new RuntimeException("Invalid token"));

    if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Token expired");
    }

    user.setPassword(
        passwordEncoder.encode(dto.getNewPassword())
    );

    user.setResetToken(null);
    user.setTokenExpiry(null);
    userRepository.save(user);

    return ResponseEntity.ok("Password reset successful");
}

@PostMapping("/change-password")
public ResponseEntity<String> changePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody ChangePasswordDto dto) {

    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() ->
            new RuntimeException("User not found"));

    // 🔐 Verify old password
    if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
        throw new RuntimeException("Current password is incorrect");
    }

    // 🔑 Update password
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    userRepository.save(user);

    return ResponseEntity.ok("Password changed successfully");
}


}
