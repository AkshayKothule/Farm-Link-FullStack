package com.farmlink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.services.RentalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // =========================
    // FARMER → CREATE REQUEST
    // =========================
    @PostMapping("/farmer")
    public ResponseEntity<String> createRentalRequest(
            @Valid @RequestBody RentalRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.createRentalRequest(
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Rental request submitted successfully");
    }

    // =========================
    // FARMER → CANCEL REQUEST
    // =========================
    @DeleteMapping("/farmer/{rentalRequestId}")
    public ResponseEntity<String> cancelRentalRequest(
            @PathVariable Long rentalRequestId,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.cancelRentalRequest(
                rentalRequestId,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Rental request cancelled successfully");
    }

    // =========================
    // OWNER → APPROVE REQUEST
    // =========================
    @PutMapping("/owner/{rentalRequestId}/approve")
    public ResponseEntity<String> approveRental(
            @PathVariable Long rentalRequestId,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.approveRental(
                rentalRequestId,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Rental request approved");
    }

    // =========================
    // OWNER → REJECT REQUEST
    // =========================
    @PutMapping("/owner/{rentalRequestId}/reject")
    public ResponseEntity<String> rejectRental(
            @PathVariable Long rentalRequestId,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.rejectRental(
                rentalRequestId,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Rental request rejected");
    }
}
