package com.farmlink.services;

import com.farmlink.dto.OwnerProfileRequestDto;
import com.farmlink.entities.Owner;

public interface OwnerService {
    Owner createOwnerProfile(OwnerProfileRequestDto ownerProfileRequestDto,
    		String email);
}
