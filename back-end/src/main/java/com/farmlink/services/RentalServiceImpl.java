package com.farmlink.services;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.BookedDateDto;
import com.farmlink.dto.FarmerRentalResponseDto;
import com.farmlink.dto.OwnerRentalResponseDto;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.entities.Equipment;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.Owner;
import com.farmlink.entities.Payment;
import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;
import com.farmlink.entities.User;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.FarmerRepository;
import com.farmlink.repository.OwnerRepository;
import com.farmlink.repository.PaymentRepository;
import com.farmlink.repository.RentalRequestRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRequestRepository rentalRepository;
    private final FarmerRepository farmerRepository;
    private final OwnerRepository ownerRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    // =====================================================
    // FARMER → CREATE RENTAL REQUEST
    // =====================================================
    @Override
    public void createRentalRequest(RentalRequestDto dto, String farmerEmail)
            throws FarmlinkCustomException {

        User user = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        // Date validation
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new FarmlinkCustomException("End date cannot be before start date");
        }

        // Prevent duplicate pending request
        if (rentalRepository.existsByFarmerIdAndEquipmentIdAndStatus(
                farmer.getId(),
                equipment.getId(),
                RentalStatus.PENDING)) {

            throw new FarmlinkCustomException(
                    "You already have a pending request for this equipment");
        }

        // Date overlap check (only APPROVED rentals block)
        boolean isAlreadyBooked =
                rentalRepository.existsActiveRentalOverlap(
                        equipment.getId(),
                        dto.getStartDate(),
                        dto.getEndDate(),
                        List.of(RentalStatus.APPROVED)
                );

        if (isAlreadyBooked) {
            throw new FarmlinkCustomException(
                    "Equipment already booked for selected dates");
        }

        RentalRequest rental = modelMapper.map(dto, RentalRequest.class);
        rental.setFarmer(farmer);
        rental.setEquipment(equipment);
        rental.setStatus(RentalStatus.PENDING);
        rental.setTotalAmount(null); // important

        rentalRepository.save(rental);
    }

    // =====================================================
    // FARMER → CANCEL RENTAL REQUEST
    // =====================================================
    @Override
    public void cancelRentalRequest(Long rentalRequestId, String farmerEmail)
            throws FarmlinkCustomException {

        User user = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        if (!rental.getFarmer().getId().equals(farmer.getId())) {
            throw new FarmlinkCustomException("You can cancel only your own rental request");
        }

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new FarmlinkCustomException("Only PENDING rentals can be cancelled");
        }

        rental.setStatus(RentalStatus.CANCELLED);
        rental.setTotalAmount(null);

        rentalRepository.save(rental);
    }

    // =====================================================
    // OWNER → APPROVE RENTAL REQUEST (IMPORTANT FIX)
    // =====================================================
    @Override
    public void approveRental(Long rentalRequestId, String ownerEmail)
            throws FarmlinkCustomException {

        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        if (!rental.getEquipment().getOwner().getId().equals(owner.getId())) {
            throw new FarmlinkCustomException("Not allowed to approve this rental");
        }

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new FarmlinkCustomException("Only PENDING rentals can be approved");
        }

        // 🔥 FINAL AMOUNT CALCULATION HERE
        long days = ChronoUnit.DAYS.between(
                rental.getStartDate(),
                rental.getEndDate()
        ) + 1;

        double totalAmount =
                rental.getEquipment().getRentPerDay() * days;

        rental.setTotalAmount(totalAmount);
        rental.setStatus(RentalStatus.APPROVED);

        rentalRepository.save(rental);
    }

    // =====================================================
    // OWNER → REJECT RENTAL REQUEST
    // =====================================================
    @Override
    public void rejectRental(Long rentalRequestId, String ownerEmail)
            throws FarmlinkCustomException {

        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        if (!rental.getEquipment().getOwner().getId().equals(owner.getId())) {
            throw new FarmlinkCustomException("Not allowed to reject this rental");
        }

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new FarmlinkCustomException("Only PENDING rentals can be rejected");
        }

        rental.setStatus(RentalStatus.REJECTED);
        rental.setTotalAmount(null);

        rentalRepository.save(rental);
    }

    // =====================================================
    // FARMER DASHBOARD
    // =====================================================
    @Override
    public List<FarmerRentalResponseDto> getFarmerRentals(String farmerEmail) {

        User user = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        return rentalRepository.findByFarmerId(farmer.getId())
                .stream()
                .map(r -> {
                    FarmerRentalResponseDto dto = new FarmerRentalResponseDto();
                    dto.setRentalId(r.getId());
                    dto.setEquipmentName(r.getEquipment().getName());
                    dto.setEquipmentCategory(r.getEquipment().getCategory());
                    dto.setOwnerName(
                        r.getEquipment().getOwner().getUser().getFirstName()
                    );
                    dto.setStartDate(r.getStartDate());
                    dto.setEndDate(r.getEndDate());
                    dto.setStatus(r.getStatus());
                    dto.setTotalAmount(r.getTotalAmount());

                    Payment payment =
                        paymentRepository.findByRentalRequest(r).orElse(null);

                    dto.setPaymentStatus(
                        payment != null ? payment.getStatus().name() : null
                    );

                    return dto;
                })

                .toList();
    }

    // =====================================================
    // OWNER DASHBOARD
    // =====================================================
    @Override
    public List<OwnerRentalResponseDto> getOwnerRentals(String ownerEmail) {

        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        return rentalRepository.findByEquipmentOwnerId(owner.getId())
                .stream()
                .map(r -> {
                    OwnerRentalResponseDto dto = new OwnerRentalResponseDto();
                    dto.setRentalId(r.getId());
                    dto.setFarmerName(r.getFarmer().getUser().getFirstName());
                    dto.setEquipmentName(r.getEquipment().getName());
                    dto.setEquipmentCategory(r.getEquipment().getCategory());
                    dto.setStartDate(r.getStartDate());
                    dto.setEndDate(r.getEndDate());
                    dto.setStatus(r.getStatus());
                    dto.setTotalAmount(r.getTotalAmount());
                    return dto;
                })
                .toList();
    }
    
    
    
    @Override
    public List<BookedDateDto> getBookedDates(Long equipmentId) {

        return rentalRepository
            .findBookedRentalsByEquipment(equipmentId)
            .stream()
            .map(r -> {
                BookedDateDto dto = new BookedDateDto();
                dto.setStartDate(r.getStartDate());
                dto.setEndDate(r.getEndDate());
                return dto;
            })
            .toList();
    }

}
