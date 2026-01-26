package com.farmlink.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.EquipmentBrowseResponseDto;
import com.farmlink.dto.EquipmentRequestDto;
import com.farmlink.dto.EquipmentResponseDto;
import com.farmlink.dto.EquipmentUpdateRequestDto;
import com.farmlink.entities.Equipment;
import com.farmlink.entities.EquipmentImage;
import com.farmlink.entities.Owner;
import com.farmlink.entities.User;
import com.farmlink.repository.EquipmentImageRepository;
import com.farmlink.repository.EquipmentRepository;
import com.farmlink.repository.OwnerRepository;
import com.farmlink.repository.RentalRequestRepository;
import com.farmlink.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
	
    private static final int MAX_IMAGES = 5;

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;
    private final RentalRequestRepository rentalRepository;
    private final CloudinaryService cloudinaryService;
    private final EquipmentImageRepository equipmentImageRepository;

    @Override
    public void addEquipment(
            EquipmentRequestDto dto,
            List<MultipartFile> images,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Owner owner = ownerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

        if (images != null && images.size() > MAX_IMAGES) {
            throw new RuntimeException("Maximum " + MAX_IMAGES + " images allowed");
        }

        Equipment equipment = modelMapper.map(dto, Equipment.class);
        equipment.setOwner(owner);
        equipment.setAvailable(true);

        equipmentRepository.save(equipment);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                validateImage(file);

                String url = cloudinaryService.upload(file);

                EquipmentImage img = new EquipmentImage();
                img.setEquipment(equipment);
                img.setImageUrl(url);

                equipmentImageRepository.save(img);
            }
        }
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Empty image not allowed");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Only image files allowed");
        }
    }

//    @Override
//    public List<EquipmentResponseDto> getMyEquipments(String email) {
//
//        // 1️⃣ Find User
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("User not found"));
//
//        // 2️⃣ Find Owner
//        Owner owner = ownerRepository.findByUserId(user.getId())
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Owner profile not found"));
//
//        // 3️⃣ Fetch equipments
//        return equipmentRepository.findByOwnerId(owner.getId())
//                .stream()
//                .map(eq -> modelMapper.map(eq, EquipmentResponseDto.class))
//                .toList();
//    }
    
    

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
public void deleteEquipment(Long equipmentId, String email)
        throws FarmlinkCustomException {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

    Owner owner = ownerRepository.findByUserId(user.getId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Owner profile not found"));

    Equipment equipment = equipmentRepository.findById(equipmentId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Equipment not found"));

    if (!equipment.getOwner().getId().equals(owner.getId())) {
        throw new FarmlinkCustomException(
                "You are not allowed to delete this equipment");
    }

    // 🚫 BLOCK if future rentals exist
    if (rentalRepository.existsApprovedFutureRental(equipmentId)) {
        throw new FarmlinkCustomException(
                "Cannot disable equipment with future approved rentals");
    }

    // ✅ SOFT DELETE
    equipment.setAvailable(false);
    equipmentRepository.save(equipment);
}

   
   
//🔹 FARMER → BROWSE AVAILABLE EQUIPMENTS
@Override
public List<EquipmentBrowseResponseDto> browseAvailableEquipments() {

    return equipmentRepository.findByAvailableTrue()
            .stream()
            .map(eq -> {

                EquipmentBrowseResponseDto dto =
                        modelMapper.map(eq, EquipmentBrowseResponseDto.class);

                dto.setOwnerBusinessName(
                        eq.getOwner().getBusinessName()
                );

                List<EquipmentImage> imageEntities =
                        equipmentImageRepository.findByEquipmentId(eq.getId());

                List<String> images = imageEntities
                        .stream()
                        .map(img -> img.getImageUrl())
                        .toList();

                dto.setImageUrls(images); // ✅ VERY IMPORTANT

                return dto;
            })
            .toList();
}


   
   
   
//   // 🔹 FARMER → BROWSE AVAILABLE EQUIPMENTS
//   public List<EquipmentResponseDto> getAvailableEquipments() {
//
//       List<Equipment> equipments =
//               equipmentRepository.findByAvailableTrue();
//
//       return equipments.stream()
//               .map(this::toResponseDto)
//               .toList();
//   }
//
//   // 🔹 Entity → DTO Mapper
//   public EquipmentResponseDto toResponseDto(Equipment e) {
//       EquipmentResponseDto dto = new EquipmentResponseDto();
//       dto.setId(e.getId());
//       dto.setName(e.getName());
//       dto.setCategory(e.getCategory());
//       dto.setRentPerDay(e.getRentPerDay());
//       dto.setDescription(e.getDescription());
//       dto.setImageUrl(e.getImageUrl());
//       dto.setOwnerName(
//           e.getOwner().getUser().getFirstName()
//       );
//       return dto;
//   }

   @Override
   public List<EquipmentResponseDto> getMyEquipments(String email) {

       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

       Owner owner = ownerRepository.findByUserId(user.getId())
               .orElseThrow(() -> new ResourceNotFoundException("Owner profile not found"));

       return equipmentRepository.findByOwnerId(owner.getId())
               .stream()
               .map(eq -> {
                   EquipmentResponseDto dto =
                           modelMapper.map(eq, EquipmentResponseDto.class);

                   // ✅ FETCH IMAGES
                   List<String> images =
                           equipmentImageRepository
                                   .findByEquipmentId(eq.getId())
                                   .stream()
                                   .map(EquipmentImage::getImageUrl)
                                   .toList();

                   dto.setImageUrls(images);
                   return dto;
               })
               .toList();
   }

    
}
