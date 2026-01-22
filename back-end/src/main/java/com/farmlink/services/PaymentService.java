package com.farmlink.services;

import com.farmlink.dto.PaymentRequestDto;

public interface PaymentService {

    void makePayment(PaymentRequestDto dto, String email);
}
