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
import com.farmlink.dto.EquipmentRequestDto;
import com.farmlink.dto.EquipmentResponseDto;
import com.farmlink.dto.EquipmentUpdateRequestDto;
import com.farmlink.services.EquipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/owners/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    //Create Equipment
    @PostMapping
    public ResponseEntity<String> addEquipment(
            @Valid @RequestBody EquipmentRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        equipmentService.addEquipment(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Equipment added successfully");
    }
    
    
    // Get all Owners Equipment
    @GetMapping
    public ResponseEntity<List<EquipmentResponseDto>> getMyEquipments(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                equipmentService.getMyEquipments(userDetails.getUsername())
        );
    }
    
    
    
    
    //Update Equipment
    @PutMapping("/{equipmentId}")
    public ResponseEntity<String> updateEquipment(
            @PathVariable("equipmentId") Long equipmentId,
            @Valid @RequestBody EquipmentUpdateRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

    	System.out.println("equipment id : "+equipmentId);
        equipmentService.updateEquipment(
                equipmentId,
                dto,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Equipment updated successfully");
    }
    
    
    
    
    //Deleting Equipment
    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<String> deleteEquipment(
            @PathVariable("equipmentId") Long equipmentId,
            @AuthenticationPrincipal UserDetails userDetails) throws FarmlinkCustomException {

        equipmentService.deleteEquipment(
                equipmentId,
                userDetails.getUsername()
        );

        return ResponseEntity.ok("Equipment deleted successfully");
    }


}
