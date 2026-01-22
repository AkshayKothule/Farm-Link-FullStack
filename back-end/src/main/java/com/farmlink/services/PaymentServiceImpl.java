package com.farmlink.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.PaymentRequestDto;
import com.farmlink.dto.PaymentRequestDto;
import com.farmlink.entities.Payment;
import com.farmlink.entities.PaymentMode;
import com.farmlink.entities.PaymentStatus;
import com.farmlink.entities.RentalRequest;
import com.farmlink.repository.PaymentRepository;
import com.farmlink.repository.RentalRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRequestRepository rentalRequestRepository;

    @Override
    public void makePayment(PaymentRequestDto dto, String email) {

        RentalRequest rental = rentalRequestRepository
                .findById(dto.getRentalRequestId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rental request not found"));

        if (paymentRepository.existsByRentalRequestId(rental.getId())) {
            throw new IllegalStateException("Payment already done");
        }

        Payment payment = new Payment();
        payment.setRentalRequest(rental);
        payment.setAmount(rental.getTotalAmount());
        payment.setPaymentMode(
                PaymentMode.valueOf(dto.getPaymentMode()));
        payment.setStatus(PaymentStatus.SUCCESS);

        paymentRepository.save(payment);
    }
}
