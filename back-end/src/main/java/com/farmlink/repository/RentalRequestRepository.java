package com.farmlink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;

public interface RentalRequestRepository
extends JpaRepository<RentalRequest, Long> {

	long countByStatus(RentalStatus status);

	
	
// Farmer dashboard
List<RentalRequest> findByFarmerId(Long farmerId);

// Owner dashboard (incoming requests)
List<RentalRequest> findByEquipmentOwnerId(Long ownerId);

// Prevent duplicate pending request
boolean existsByFarmerIdAndEquipmentIdAndStatus(
    Long farmerId,
    Long equipmentId,
    RentalStatus status
);
}
