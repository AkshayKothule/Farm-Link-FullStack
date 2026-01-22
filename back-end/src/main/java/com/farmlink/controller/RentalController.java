
package com.farmlink.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.FarmerRentalResponseDto;
import com.farmlink.dto.OwnerRentalResponseDto;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.entities.RentalRequest;
import com.farmlink.services.RentalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // FARMER → CREATE
    @PostMapping("/farmer")
    public ResponseEntity<String> createRental(
            @Valid @RequestBody RentalRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.createRentalRequest(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Rental request submitted");
    }

    // FARMER → CANCEL
    @DeleteMapping("/farmer/{id}")
    public ResponseEntity<String> cancelRental(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.cancelRentalRequest(id, userDetails.getUsername());
        return ResponseEntity.ok("Rental request cancelled");
    }

    // FARMER DASHBOARD
    @GetMapping("/farmer")
    public ResponseEntity<List<FarmerRentalResponseDto>> farmerDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                rentalService.getFarmerRentals(userDetails.getUsername()));
    }


 // OWNER DASHBOARD
    @GetMapping("/owner")
    public ResponseEntity<List<OwnerRentalResponseDto>> ownerDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                rentalService.getOwnerRentals(userDetails.getUsername()));
    }

    // OWNER → APPROVE
    @PutMapping("/owner/{id}/approve")
    public ResponseEntity<String> approve(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.approveRental(id, userDetails.getUsername());
        return ResponseEntity.ok("Rental approved");
    }

    // OWNER → REJECT
    @PutMapping("/owner/{id}/reject")
    public ResponseEntity<String> reject(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        rentalService.rejectRental(id, userDetails.getUsername());
        return ResponseEntity.ok("Rental rejected");
    }
}
