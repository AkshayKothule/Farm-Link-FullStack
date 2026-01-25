package com.farmlink.services;

import java.util.List;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.BookedDateDto;
import com.farmlink.dto.FarmerRentalResponseDto;
import com.farmlink.dto.OwnerRentalResponseDto;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.entities.RentalRequest;
 

public interface RentalService {

    // FARMER
    void createRentalRequest(RentalRequestDto dto, String farmerEmail) throws FarmlinkCustomException;
    void cancelRentalRequest(Long rentalRequestId, String farmerEmail) throws FarmlinkCustomException ;
    List<FarmerRentalResponseDto> getFarmerRentals(String farmerEmail);
;

    // OWNER
    void approveRental(Long rentalRequestId, String ownerEmail)throws FarmlinkCustomException;
    void rejectRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException ;
    List<OwnerRentalResponseDto> getOwnerRentals(String ownerEmail);
    
    
    public List<BookedDateDto> getBookedDates(Long equipmentId);
}

