package com.farmlink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByOwnerId(Long ownerId);
    List<Equipment> findByAvailableTrue();
}
