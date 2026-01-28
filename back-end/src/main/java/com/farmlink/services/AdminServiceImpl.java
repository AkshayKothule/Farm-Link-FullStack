package com.farmlink.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.AdminDashboardResponseDto;
import com.farmlink.dto.AdminFarmerResponseDto;
import com.farmlink.dto.AdminOwnerResponseDto;
import com.farmlink.dto.AdminReviewResponseDto;
import com.farmlink.entities.Owner;
import com.farmlink.entities.RentalStatus;
import com.farmlink.entities.UserRole;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.OwnerRepository;
import com.farmlink.repository.RentalRequestRepository;
import com.farmlink.repository.ReviewRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final OwnerRepository ownerRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final RentalRequestRepository rentalRepository;


    @Override
    public void verifyOwner(Long ownerId) {

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Owner not found"));

        owner.setVerified(true);
        ownerRepository.save(owner);
    }

    @Override
    public void deleteReview(Long reviewId) {

        if (!reviewRepository.existsById(reviewId)) {
            throw new ResourceNotFoundException("Review not found");
        }

        reviewRepository.deleteById(reviewId);
    }
    
    
    

    
    @Override
    public AdminDashboardResponseDto getDashboardData() {

        AdminDashboardResponseDto dto = new AdminDashboardResponseDto();

        // USERS
        dto.setTotalUsers(userRepository.count());
        dto.setTotalFarmers(userRepository.countByRole(UserRole.FARMER));
        dto.setTotalOwners(userRepository.countByRole(UserRole.OWNER));
        dto.setPendingOwners(ownerRepository.countByVerifiedFalse());

        // EQUIPMENTS
        dto.setTotalEquipments(equipmentRepository.count());
        dto.setAvailableEquipments(
                equipmentRepository.countByAvailableTrue());

        // RENTALS
        dto.setTotalRentals(rentalRepository.count());
        dto.setApprovedRentals(
                rentalRepository.countByStatus(RentalStatus.APPROVED));
        dto.setRejectedRentals(
                rentalRepository.countByStatus(RentalStatus.REJECTED));

        // REVIEWS
        dto.setTotalReviews(reviewRepository.count());
        Double avg = reviewRepository.findOverallAverageRating();
        dto.setAverageRatingOverall(avg != null ? avg : 0.0);

        return dto;
    }
    
    
    @Override
    public List<AdminOwnerResponseDto> getAllOwners() {

        return ownerRepository.findAll()
            .stream()
            .map(owner -> {
                AdminOwnerResponseDto dto = new AdminOwnerResponseDto();

                dto.setId(owner.getId());
                dto.setBusinessName(owner.getBusinessName());
                dto.setVerified(owner.getVerified());

                dto.setUserName(
                    owner.getUser().getFirstName() + " " +
                    owner.getUser().getLastName()
                );

                return dto;
            })
            .toList();
    }

    
    @Override
    public List<AdminFarmerResponseDto> getAllFarmers() {

        return userRepository.findByRole(UserRole.FARMER)
            .stream()
            .map(user -> {
                AdminFarmerResponseDto dto = new AdminFarmerResponseDto();

                dto.setId(user.getId());
                dto.setEmail(user.getEmail());
                dto.setName(
                    user.getFirstName() + " " + user.getLastName()
                );

                return dto;
            })
            .toList();
    }
    
    
    @Override
    public List<AdminReviewResponseDto> getAllReviews() {

        return reviewRepository.findAll()
            .stream()
            .map(r -> {
                AdminReviewResponseDto dto =
                    new AdminReviewResponseDto();

                dto.setId(r.getId());
                dto.setComment(r.getComment());
                dto.setRating(r.getRating());

                dto.setUserName(
                    r.getFarmer().getUser().getFirstName() + " " +
                    r.getFarmer().getUser().getLastName()
                );

                return dto;
            })
            .toList();
    }


    
}
