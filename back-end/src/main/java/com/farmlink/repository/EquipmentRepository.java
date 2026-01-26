package com.farmlink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.Equipment;

import lombok.Getter;
import lombok.Setter;


public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByOwnerId(Long ownerId);
    
    long countByAvailableTrue();
    
    // 🔹 Only available equipments for farmers
    List<Equipment> findByAvailableTrue();

}
