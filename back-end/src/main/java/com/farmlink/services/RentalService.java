package com.farmlink.services;

import com.farmlink.customexception.FarmlinkCustomException;
import com.farmlink.dto.RentalRequestDto;
 

public interface RentalService {

    // Farmer side
    void createRentalRequest(RentalRequestDto dto, String farmerEmail) throws FarmlinkCustomException ;
     void cancelRentalRequest(Long rentalRequestId, String farmerEmail) throws FarmlinkCustomException ;

    // Owner side
    void approveRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException ;
    void rejectRental(Long rentalRequestId, String ownerEmail) throws FarmlinkCustomException ;
}
