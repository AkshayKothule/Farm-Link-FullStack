package com.farmlink.services;

import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.entities.Farmer;

public interface FarmerService {
	public Farmer createFarmerProfile(
            FarmerProfileRequestDto farmerProfileRequestDto,
            String email);
}
