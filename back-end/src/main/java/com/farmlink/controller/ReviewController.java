package com.farmlink.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmlink.dto.ReviewRequestDto;
import com.farmlink.dto.ReviewResponseDto;
import com.farmlink.services.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<String> addReview(
            @Valid @RequestBody ReviewRequestDto reviewRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {


        reviewService.addReview(reviewRequestDto, userDetails.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Review added successfully");    }
    
    
    
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        reviewService.updateReview(reviewId, dto, userDetails.getUsername());

        return ResponseEntity.ok("Review updated successfully");
    }

    
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {

        reviewService.deleteReviewByAdmin(reviewId, userDetails.getUsername());

        return ResponseEntity.ok("Review deleted by admin");
    }

    
    @GetMapping("/equipment/{equipmentId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(
            @PathVariable Long equipmentId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByEquipment(equipmentId)
        );
    }

    @GetMapping("/equipment/{equipmentId}/avg")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long equipmentId) {

        return ResponseEntity.ok(
                reviewService.getAverageRating(equipmentId)
        );
    }

}
