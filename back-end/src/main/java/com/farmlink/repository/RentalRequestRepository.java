package com.farmlink.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;

public interface RentalRequestRepository
        extends JpaRepository<RentalRequest, Long> {

    /* =========================
       Dashboard Queries
       ========================= */

    // Farmer dashboard
    List<RentalRequest> findByFarmerId(Long farmerId);

    // Owner dashboard (incoming requests)
    List<RentalRequest> findByEquipmentOwnerId(Long ownerId);

    // Admin / summary
    long countByStatus(RentalStatus status);


    /* =========================
       Business Rules
       ========================= */

    // ✅ Prevent duplicate PENDING request
    boolean existsByFarmerIdAndEquipmentIdAndStatus(
            Long farmerId,
            Long equipmentId,
            RentalStatus status
    );

    // ✅ Date-wise availability check (industry standard)
    @Query("""
    SELECT COUNT(r) > 0 FROM RentalRequest r
    WHERE r.equipment.id = :equipmentId
      AND r.status IN :statuses
      AND r.startDate <= :endDate
      AND r.endDate >= :startDate
    """)
    boolean existsActiveRentalOverlap(
            @Param("equipmentId") Long equipmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("statuses") List<RentalStatus> statuses
    );
}

