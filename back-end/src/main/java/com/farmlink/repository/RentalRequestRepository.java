package com.farmlink.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.RentalRequest;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

}

