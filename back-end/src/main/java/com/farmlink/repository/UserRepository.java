package com.farmlink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmlink.entities.User;
import com.farmlink.entities.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {

    // find user for login
    Optional<User> findByEmail(String email);

    // validation checks
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
    
    long countByRole(UserRole role);

}
