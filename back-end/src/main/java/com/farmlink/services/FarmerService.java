package com.farmlink.services;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.FarmerProfileRequestDto;
import com.farmlink.dto.FarmerProfileResponseDto;
import com.farmlink.dto.FarmerProfileUpdateDto;
import com.farmlink.entities.Farmer;


public interface FarmerService  {
	 public void createFarmerProfile(
	            FarmerProfileRequestDto dto, String email) throws FarmlinkCustomException;
	

	public FarmerProfileResponseDto getMyProfile(String email);

	 public void updateFarmerProfile(
	            FarmerProfileUpdateDto dto,
	            String email);
}
