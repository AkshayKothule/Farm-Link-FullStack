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

    // Farmer dashboard
    List<RentalRequest> findByFarmerId(Long farmerId);

    // Owner dashboard
    List<RentalRequest> findByEquipmentOwnerId(Long ownerId);

    // Prevent duplicate pending request
    boolean existsByFarmerIdAndEquipmentIdAndStatus(
            Long farmerId,
            Long equipmentId,
            RentalStatus status
    );

    // Overlap check (used while creating rental)
    @Query("""
        SELECT COUNT(r) > 0
        FROM RentalRequest r
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

    // 🔥 FINAL BOOKED DATES QUERY
    @Query("""
        SELECT r
        FROM RentalRequest r
        WHERE r.equipment.id = :equipmentId
          AND r.status IN (
            com.farmlink.entities.RentalStatus.APPROVED,
            com.farmlink.entities.RentalStatus.COMPLETED
          )
    """)
    List<RentalRequest> findBookedRentalsByEquipment(
        @Param("equipmentId") Long equipmentId
    );
    
    @Query("""
    	    SELECT COUNT(r) > 0
    	    FROM RentalRequest r
    	    WHERE r.equipment.id = :equipmentId
    	      AND r.status = com.farmlink.entities.RentalStatus.APPROVED
    	      AND r.endDate >= CURRENT_DATE
    	""")
    	boolean existsApprovedFutureRental(
    	    @Param("equipmentId") Long equipmentId
    	);

}
