package com.farmlink.services;

import java.util.List;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.EquipmentBrowseResponseDto;
import com.farmlink.dto.EquipmentRequestDto;
import com.farmlink.dto.EquipmentResponseDto;
import com.farmlink.dto.EquipmentUpdateRequestDto;
import com.farmlink.entities.Equipment;

public interface EquipmentService {

    public Equipment addEquipment(EquipmentRequestDto dto, String email);
    
    public List<EquipmentResponseDto> getMyEquipments(String email);
    
    public void updateEquipment(
            Long equipmentId,
            EquipmentUpdateRequestDto dto,
            String email) throws FarmlinkCustomException ;
    
    public void deleteEquipment(Long equipmentId, String email) throws FarmlinkCustomException ;
    
    //equipmentBroweResponeseDto
    
    List<EquipmentBrowseResponseDto> browseAvailableEquipments();
    
//    private EquipmentResponseDto toResponseDto(Equipment e)
    
    
    

}
