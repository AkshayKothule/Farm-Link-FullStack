package com.farmlink.services;

import org.springframework.stereotype.Service;


import com.farmlink.customexception.ResourceAlreadyExists;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.entities.Farmer;
import com.farmlink.entities.User;
import com.farmlink.repository.FarmerRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Farmer createFarmerProfile(
            FarmerProfileRequestDto farmerProfileRequestDto,
            String email) {

        // 1️⃣ User find
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Farmer already exists?
        farmerRepository.findByUserId(user.getId())
                .ifPresent(f -> {
                    throw new ResourceAlreadyExists("Farmer already exists");
                });

        // 3️⃣ DTO → Entity (ModelMapper)
        Farmer farmer = modelMapper.map(
                farmerProfileRequestDto, Farmer.class);

        // 4️⃣ Mandatory association
        farmer.setUser(user);

        // 5️⃣ Save
        return farmerRepository.save(farmer);
    }
}
