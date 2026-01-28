package com.farmlink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByUserId(Long userId);
    
    long countByVerifiedFalse();

}
