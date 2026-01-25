package com.farmlink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.farmlink.entities.Payment;
import com.farmlink.entities.RentalRequest;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByRentalRequestId(Long rentalRequestId);

    Optional<Payment> findByRentalRequestId(Long rentalRequestId);
    
    // 🔥 REQUIRED for Razorpay verification
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    
    Optional<Payment> findByRentalRequest(RentalRequest rentalRequest);

}
