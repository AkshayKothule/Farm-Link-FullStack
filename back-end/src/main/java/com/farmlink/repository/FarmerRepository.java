package com.farmlink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long>{

	Optional<Farmer> findByUserId(Long userId);
}
