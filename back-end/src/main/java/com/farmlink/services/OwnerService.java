package com.farmlink.services;

import com.farmlink.dto.OwnerProfileRequestDto;
import com.farmlink.dto.OwnerProfileResponseDto;
import com.farmlink.dto.OwnerProfileUpdateDto;
import com.farmlink.entities.Owner;

public interface OwnerService {
    Owner createOwnerProfile(OwnerProfileRequestDto ownerProfileRequestDto,
    		String email);
    
    public OwnerProfileResponseDto getMyProfile(String email);
    
    public void updateOwnerProfile(
            OwnerProfileUpdateDto dto,
            String email);
}
