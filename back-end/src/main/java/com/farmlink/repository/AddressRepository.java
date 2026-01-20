package com.farmlink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.Address;
import com.farmlink.entities.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // Get all addresses of a user (future proof)
    List<Address> findByUser(User user);

    // Get address by user (if only one address per user)
    Optional<Address> findByUserId(Long userId);

    // Check if address already exists for user (optional safety)
    boolean existsByUserId(Long userId);
}
