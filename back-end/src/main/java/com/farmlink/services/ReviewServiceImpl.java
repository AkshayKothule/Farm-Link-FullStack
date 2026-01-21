package com.farmlink.services;
import java.util.List;

import org.springframework.stereotype.Service;

import com.farmlink.customexception.AuthenticationException;
import com.farmlink.customexception.ResourceAlreadyExists;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.ReviewRequestDto;
import com.farmlink.dto.ReviewResponseDto;
import com.farmlink.entities.Equipment;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.Review;
import com.farmlink.entities.User;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.FarmerRepository;
import com.farmlink.repository.ReviewRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final FarmerRepository farmerRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;

    //add review
    @Override
    public void addReview(ReviewRequestDto dto, String username) {

        // 1️⃣ username (email) वरून User fetch कर
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ User वरून Farmer fetch कर
        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer profile not found"));

        // 3️⃣ Equipment fetch कर
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipment not found"));

        // 4️⃣ Duplicate review check
        reviewRepository
                .findByFarmerIdAndEquipmentId(farmer.getId(), equipment.getId())
                .ifPresent(r -> {
                    throw new ResourceAlreadyExists("Review already given for this equipment");
                });

        // 5️⃣ Review save कर
        Review review = new Review();
        review.setFarmer(farmer);
        review.setEquipment(equipment);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);
    }


    @Override
    public void updateReview(Long reviewId,
                             ReviewRequestDto dto,
                             String username) throws AuthenticationException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer profile not found"));

        Review review = reviewRepository
                .findByIdAndFarmerId(reviewId, farmer.getId())
                .orElseThrow(() ->
                        new AuthenticationException("You can update only your own review"));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);
    }

    
    @Override
    public void deleteReviewByAdmin(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }

    
    @Override
    public List<ReviewResponseDto> getReviewsByEquipment(Long equipmentId) {

        List<Review> reviews = reviewRepository.findByEquipmentId(equipmentId);

        return reviews.stream()
                .map(r -> new ReviewResponseDto(
                        r.getRating(),
                        r.getComment(),
                        r.getFarmer().getUser().getFirstName()
                ))
                .toList();
    }

    @Override
    public Double getAverageRating(Long equipmentId) {

        Double avg = reviewRepository.findAverageRatingByEquipmentId(equipmentId);

        return avg != null ? avg : 0.0;
    }



}
