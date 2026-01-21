package com.farmlink.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.EquipmentBrowseResponseDto;
import com.farmlink.dto.EquipmentRequestDto;
import com.farmlink.dto.EquipmentResponseDto;
import com.farmlink.dto.EquipmentUpdateRequestDto;
import com.farmlink.entities.Equipment;
import com.farmlink.entities.Owner;
import com.farmlink.entities.User;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.OwnerRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;

    @Override
    public Equipment addEquipment(EquipmentRequestDto dto, String email) {

        // 1️⃣ Find logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Get owner profile (must exist)
        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Owner profile not found"));

        // 3️⃣ DTO → Entity
        Equipment equipment = modelMapper.map(dto, Equipment.class);

        // 4️⃣ Mandatory system fields
        equipment.setOwner(owner);
        equipment.setAvailable(true);

        // 5️⃣ Save
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<EquipmentResponseDto> getMyEquipments(String email) {

        // 1️⃣ Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // 2️⃣ Find Owner
        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Owner profile not found"));

        // 3️⃣ Fetch equipments
        return equipmentRepository.findByOwnerId(owner.getId())
                .stream()
                .map(eq -> modelMapper.map(eq, EquipmentResponseDto.class))
                .toList();
    }
    
    

   @Override
   	public void updateEquipment(
        Long equipmentId,
        EquipmentUpdateRequestDto dto,
        String email) throws FarmlinkCustomException {

    // 1️⃣ Find User
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    // 2️⃣ Find Owner
    Owner owner = ownerRepository.findByUserId(user.getId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Owner profile not found"));

    // 3️⃣ Find Equipment
    Equipment equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Equipment not found"));

    // 4️⃣ Ownership check
    if (!equipment.getOwner().getId().equals(owner.getId())) {
        throw new FarmlinkCustomException(
                "You are not allowed to update this equipment");
    }

    // 5️⃣ DTO → EXISTING ENTITY (SAFE)
    modelMapper.map(dto, equipment);

    // 6️⃣ Handle nullable boolean explicitly
    if (dto.getAvailable() == null) {
        // keep existing value
        equipment.setAvailable(equipment.getAvailable());
    }

    // 7️⃣ Save
    equipmentRepository.save(equipment);
   	}

    
    
   @Override
   public void deleteEquipment(Long equipmentId, String email) throws FarmlinkCustomException {

       // 1️⃣ Find User
       User user = userRepository.findByEmail(email)
               .orElseThrow(() ->
                       new ResourceNotFoundException("User not found"));

       // 2️⃣ Find Owner
       Owner owner = ownerRepository.findByUserId(user.getId())
               .orElseThrow(() ->
                       new ResourceNotFoundException("Owner profile not found"));

       // 3️⃣ Find Equipment
       Equipment equipment = equipmentRepository.findById(equipmentId)
               .orElseThrow(() ->
                       new ResourceNotFoundException("Equipment not found"));

       // 4️⃣ OWNERSHIP CHECK (🔥 MOST IMPORTANT)
       if (!equipment.getOwner().getId().equals(owner.getId())) {
           throw new FarmlinkCustomException(
                   "You are not allowed to delete this equipment");
       }

       // 5️⃣ SAFE DELETE
       equipmentRepository.delete(equipment);
   }
   
   
   //rental check 
   @Override
   public List<EquipmentBrowseResponseDto> browseAvailableEquipments() {

       return equipmentRepository.findByAvailableTrue()
               .stream()
               .map(eq -> {
                   EquipmentBrowseResponseDto dto =
                           modelMapper.map(eq, EquipmentBrowseResponseDto.class);

                   // Owner display info
                   dto.setOwnerBusinessName(
                           eq.getOwner().getBusinessName());

                   return dto;
               })
               .toList();
   }


    
}
