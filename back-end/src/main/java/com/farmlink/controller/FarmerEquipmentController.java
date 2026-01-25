package com.farmlink.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.BookedDateDto;
import com.farmlink.dto.EquipmentBrowseResponseDto;
import com.farmlink.services.EquipmentService;
import com.farmlink.services.RentalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/farmers/equipments")
@RequiredArgsConstructor
public class FarmerEquipmentController {

    private final EquipmentService equipmentService;
    private final RentalService rentalService;

    // ===============================
    // ✅ BROWSE AVAILABLE EQUIPMENTS
    // ===============================
    @GetMapping("/available")
    public ResponseEntity<List<EquipmentBrowseResponseDto>> browseAvailableEquipments() {

        return ResponseEntity.ok(
            equipmentService.browseAvailableEquipments()
        );
    }

    // ===============================
    // ✅ GET BOOKED DATES FOR EQUIPMENT
    // ===============================
    @GetMapping("/{equipmentId}/booked-dates")
    public ResponseEntity<List<BookedDateDto>> getBookedDates(
            @PathVariable("equipmentId") Long equipmentId) {

        return ResponseEntity.ok(
            rentalService.getBookedDates(equipmentId)
        );
    }
}
