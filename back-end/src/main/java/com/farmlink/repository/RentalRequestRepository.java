package com.farmlink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

	long countByStatus(RentalStatus status);

	
	
}

