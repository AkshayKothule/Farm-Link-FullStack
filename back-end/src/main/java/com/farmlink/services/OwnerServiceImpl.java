package com.farmlink.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.OwnerProfileRequestDto;
import com.farmlink.dto.OwnerProfileResponseDto;
import com.farmlink.dto.OwnerProfileUpdateDto;
import com.farmlink.entities.Owner;
import com.farmlink.entities.User;
import com.farmlink.repository.OwnerRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Owner createOwnerProfile(
            OwnerProfileRequestDto ownerProfileRequestDto,
            String email) {

    	System.out.println(ownerProfileRequestDto);
        // 1️⃣ Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Lazy create Owner
        return ownerRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    // 3️⃣ DTO → Entity
                    Owner owner = modelMapper.map(
                            ownerProfileRequestDto,
                            Owner.class
                    );

                    // 4️⃣ Mandatory + system fields
                    owner.setUser(user);
                    owner.setVerified(false);   // 👈 isValid / verified set here
                    System.out.println(owner);
                    return ownerRepository.save(owner);
                });
    }
    
    
    
    
    @Override
    @Transactional
    public OwnerProfileResponseDto getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Owner profile not found"));

        OwnerProfileResponseDto dto = new OwnerProfileResponseDto();

        // USER
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());

        // OWNER
        dto.setBusinessName(owner.getBusinessName());
        dto.setVerified(owner.getVerified());

        return dto;
    }
    
    
    
    @Override
    @Transactional
    public void updateOwnerProfile(
            OwnerProfileUpdateDto dto,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Owner profile not found"));

        // USER update
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());

        // OWNER update
        owner.setBusinessName(dto.getBusinessName());

        userRepository.save(user);
        ownerRepository.save(owner);
    }



}
