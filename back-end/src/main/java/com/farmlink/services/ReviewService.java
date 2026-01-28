package com.farmlink.services;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.farmlink.customexception.AuthenticationException;
import com.farmlink.dto.AdminReviewResponseDto;
import com.farmlink.dto.ReviewRequestDto;
import com.farmlink.dto.AdminReviewResponseDto;

public interface ReviewService {

	    public void addReview(ReviewRequestDto reviewRequestDto, String username);
	
        public void updateReview(Long reviewId, ReviewRequestDto dto, String username) throws AuthenticationException;
        
        public void deleteReviewByAdmin(Long reviewId, String email) throws AccessDeniedException ;        
        public List<AdminReviewResponseDto> getReviewsByEquipment(Long equipmentId);

        public Double getAverageRating(Long equipmentId);

}
