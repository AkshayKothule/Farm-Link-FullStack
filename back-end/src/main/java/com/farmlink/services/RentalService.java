package com.farmlink.services;

import java.util.List;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.RentalRequestDto;
import com.farmlink.entities.RentalRequest;
 

public interface RentalService {

    // FARMER
    void createRentalRequest(RentalRequestDto dto, String farmerEmail) throws FarmlinkCustomException;
    void cancelRentalRequest(Long rentalRequestId, String farmerEmail) throws FarmlinkCustomException ;
    List<RentalRequest> getFarmerRentals(String farmerEmail);

    // OWNER
    void approveRental(Long rentalRequestId, String ownerEmail)throws FarmlinkCustomException;
    void rejectRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException ;
    List<RentalRequest> getOwnerRentals(String ownerEmail);
}

