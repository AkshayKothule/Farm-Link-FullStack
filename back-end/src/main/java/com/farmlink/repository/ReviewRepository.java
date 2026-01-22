package com.farmlink.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.farmlink.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // All reviews for a specific equipment (public view)
    List<Review> findByEquipmentId(Long equipmentId);

    // To prevent duplicate review by same farmer for same equipment
    Optional<Review> findByFarmerIdAndEquipmentId(Long farmerId, Long equipmentId);
    
    Optional<Review> findByIdAndFarmerId(Long reviewId, Long farmerId);
    

    
    // Average rating of an equipment
    @Query("select avg(r.rating) from Review r where r.equipment.id = :equipmentId")
    Double findAverageRatingByEquipmentId(Long equipmentId);
    
    @Query("SELECT AVG(r.rating) FROM Review r")
    Double findOverallAverageRating();

}
