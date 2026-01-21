
package com.farmlink.services;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.entities.Equipment;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.Owner;
import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;
import com.farmlink.entities.User;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.FarmerRepository;
import com.farmlink.repository.OwnerRepository;
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
    private final ModelMapper modelMapper;

    // =========================
    // FARMER → CREATE REQUEST
    // =========================
    @Override
    public void createRentalRequest(RentalRequestDto dto, String farmerEmail) throws FarmlinkCustomException {

        User user = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        if (!Boolean.TRUE.equals(equipment.getAvailable())) {
            throw new FarmlinkCustomException("Equipment not available");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new FarmlinkCustomException("Invalid rental dates");
        }

        RentalRequest rental = modelMapper.map(dto, RentalRequest.class);
        rental.setFarmer(farmer);
        rental.setEquipment(equipment);
        rental.setStatus(RentalStatus.REQUESTED);

        rentalRepository.save(rental);
    }

    // =========================
    // FARMER → CANCEL REQUEST
    // =========================
    @Override
    public void cancelRentalRequest(Long rentalRequestId, String farmerEmail) throws FarmlinkCustomException {

        User user = userRepository.findByEmail(farmerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        // 🔐 Ownership check
        if (!rental.getFarmer().getId().equals(farmer.getId())) {
            throw new FarmlinkCustomException("You cannot cancel this rental request");
        }

        // 🔥 Status rule
        if (rental.getStatus() != RentalStatus.REQUESTED) {
            throw new FarmlinkCustomException(
                    "Only REQUESTED rentals can be cancelled");
        }

        rental.setStatus(RentalStatus.CANCELLED);
        rentalRepository.save(rental);
    }

    // =========================
    // OWNER → APPROVE REQUEST
    // =========================
    @Override
    public void approveRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException {

        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        // 🔐 Ownership check
        if (!rental.getEquipment().getOwner().getId().equals(owner.getId())) {
            throw new FarmlinkCustomException(
                    "You are not allowed to approve this rental");
        }

        if (rental.getStatus() != RentalStatus.REQUESTED) {
            throw new FarmlinkCustomException(
                    "Only REQUESTED rentals can be approved");
        }

        rental.setStatus(RentalStatus.APPROVED);
        rental.getEquipment().setAvailable(false);

        rentalRepository.save(rental);
    }

    // =========================
    // OWNER → REJECT REQUEST
    // =========================
    @Override
    public void rejectRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException {

        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        RentalRequest rental = rentalRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        // 🔐 Ownership check
        if (!rental.getEquipment().getOwner().getId().equals(owner.getId())) {
            throw new FarmlinkCustomException(
                    "You are not allowed to reject this rental");
        }

        if (rental.getStatus() != RentalStatus.REQUESTED) {
            throw new FarmlinkCustomException(
                    "Only REQUESTED rentals can be rejected");
        }

        rental.setStatus(RentalStatus.REJECTED);
        rentalRepository.save(rental);
    }
}
