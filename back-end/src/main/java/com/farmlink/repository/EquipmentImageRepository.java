package com.farmlink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.EquipmentImage;

public interface EquipmentImageRepository
extends JpaRepository<EquipmentImage, Long> {

List<EquipmentImage> findByEquipmentId(Long equipmentId);
}

