package com.farmlink.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceAlreadyExists;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.dto.FarmerProfileResponseDto;
import com.farmlink.dto.FarmerProfileUpdateDto;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.User;
import com.farmlink.repository.FarmerRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // ===============================
    // CREATE FARMER PROFILE
    // ===============================
    @Override
    public void createFarmerProfile(
            FarmerProfileRequestDto dto,
            String email) {

        // 1️⃣ Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Check if Farmer already exists
        farmerRepository.findByUserId(user.getId())
                .ifPresent(f -> {
                    throw new ResourceAlreadyExists(
                            "Farmer profile already exists");
                });

        // 3️⃣ DTO → Entity
        Farmer farmer = modelMapper.map(dto, Farmer.class);

        // 4️⃣ Mandatory association
        farmer.setUser(user);

        // 5️⃣ Save
        farmerRepository.save(farmer);
    }

    // ===============================
    // READ FARMER PROFILE
    // ===============================
    @Override
    public FarmerProfileResponseDto getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer profile not found"));

        return modelMapper.map(
                farmer, FarmerProfileResponseDto.class);
    }

    // ===============================
    // UPDATE FARMER PROFILE
    // ===============================
    @Override
    @Transactional
    public void updateFarmerProfile(
            FarmerProfileUpdateDto dto,
            String email) {

        // 1️⃣ Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Find Farmer
        Farmer farmer = farmerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Farmer profile not found"));

        // 3️⃣ Update USER (explicit whitelist)
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());

        // 4️⃣ Update FARMER
        farmer.setFarmType(dto.getFarmType());
        farmer.setLandArea(dto.getLandArea());

        // 5️⃣ Save (transactional boundary)
        userRepository.save(user);
        farmerRepository.save(farmer);
    }

}
