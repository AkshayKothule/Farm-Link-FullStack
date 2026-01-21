package com.farmlink.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.EquipmentBrowseResponseDto;
import com.farmlink.services.EquipmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/farmers/equipments")
@RequiredArgsConstructor
public class FarmerEquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentBrowseResponseDto>> browseEquipments() {

        return ResponseEntity.ok(
                equipmentService.browseAvailableEquipments()
        );
    }
}

