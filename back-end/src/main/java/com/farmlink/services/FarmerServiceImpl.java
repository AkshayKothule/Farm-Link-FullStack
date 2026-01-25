package com.farmlink.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.FarmlinkCustomException;
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
            FarmerProfileRequestDto dto, String email) throws FarmlinkCustomException {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found")
            );

        if (farmerRepository.findByUserId(user.getId()).isPresent()) {
            throw new FarmlinkCustomException("Profile already exists");
        }

        Farmer farmer = modelMapper.map(dto, Farmer.class);
        farmer.setUser(user);
        farmerRepository.save(farmer);
    }

    // ===============================
    // READ FARMER PROFILE (AUTO-CREATE)
    // ===============================
    @Override
    public FarmerProfileResponseDto getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found")
            );

        Farmer farmer = farmerRepository.findByUserId(user.getId())
            .orElseGet(() -> {
                Farmer f = new Farmer();
                f.setUser(user);
                f.setFarmType("Not specified");
                f.setLandArea(0.0);
               
                return farmerRepository.save(f);
            });

        FarmerProfileResponseDto dto = new FarmerProfileResponseDto();

        // ===== USER DATA =====
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());

        // ===== FARMER DATA =====
        dto.setFarmType(farmer.getFarmType());
        dto.setLandArea(farmer.getLandArea());
        

        return dto;
    }

    // ===============================
    // UPDATE FARMER PROFILE
    // ===============================
    @Override
    public void updateFarmerProfile(FarmerProfileUpdateDto dto, String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("User not found")
            );

        // ===== UPDATE USER =====
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());

        userRepository.save(user);

        // ===== UPDATE FARMER =====
        Farmer farmer = farmerRepository.findByUserId(user.getId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Farmer profile not found")
            );

        farmer.setFarmType(dto.getFarmType());
        farmer.setLandArea(dto.getLandArea());
       

        farmerRepository.save(farmer);
    }

}
