package com.farmlink.services;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceNotFoundException;
import com.farmlink.dto.PaymentHistoryDto;
import com.farmlink.dto.PaymentOrderResponseDto;
import com.farmlink.dto.PaymentVerifyRequestDto;
import com.farmlink.entities.Payment;
import com.farmlink.entities.PaymentStatus;
import com.farmlink.entities.RentalRequest;
import com.farmlink.entities.RentalStatus;
import com.farmlink.repository.PaymentRepository;
import com.farmlink.repository.RentalRequestRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;
    private final RentalRequestRepository rentalRequestRepository;

    // STEP 1: CREATE RAZORPAY ORDER
@Override
public PaymentOrderResponseDto createOrder(
        Long rentalId, String email) throws RazorpayException {

    RentalRequest rental = rentalRequestRepository.findById(rentalId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Rental request not found"));

    // Farmer ownership check
    if (!rental.getFarmer()
               .getUser()
               .getEmail()
               .equals(email)) {
        throw new AccessDeniedException(
                "You are not allowed to pay for this rental");
    }

    // Only APPROVED rentals can be paid
    if (rental.getStatus() != RentalStatus.APPROVED) {
        throw new IllegalStateException(
                "Payment allowed only for approved rentals");
    }

    // 🔥 IMPORTANT FIX: reuse CREATED payment
    Payment existingPayment = paymentRepository
            .findByRentalRequestId(rentalId)
            .orElse(null);

    if (existingPayment != null) {

        // ❌ Already paid
        if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Payment already completed");
        }

        // ✅ CREATED → resume same order
        return new PaymentOrderResponseDto(
                existingPayment.getRazorpayOrderId(),
                keyId,
                existingPayment.getAmount()
        );
    }

    // ===============================
    // CREATE NEW RAZORPAY ORDER
    // ===============================
    double amount = rental.getTotalAmount();

   
        RazorpayClient client =
                new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", amount * 100); // INR → paise
        options.put("currency", "INR");
        options.put("receipt", "rental_" + rentalId);

        Order order = client.orders.create(options);

        Payment payment = new Payment();
        payment.setRentalRequest(rental);
        payment.setAmount(amount);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setStatus(PaymentStatus.CREATED);

        paymentRepository.save(payment);

        return new PaymentOrderResponseDto(
                order.get("id"),
                keyId,
                amount
        );

    
}

    // STEP 2: VERIFY PAYMENT
@Override
public void verifyPayment(PaymentVerifyRequestDto dto) {

    Payment payment = paymentRepository
            .findByRazorpayOrderId(dto.getOrderId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Payment not found"));

    if (payment.getStatus() == PaymentStatus.SUCCESS) {
        return; // already verified (idempotent)
    }

    payment.setRazorpayPaymentId(dto.getPaymentId());
    payment.setRazorpaySignature(dto.getSignature());
    payment.setStatus(PaymentStatus.SUCCESS);

    // Complete rental
    payment.getRentalRequest()
           .setStatus(RentalStatus.COMPLETED);

    paymentRepository.save(payment);
}

@Override
public List<PaymentHistoryDto> getFarmerPayments(String email) {

    return paymentRepository
            .findByRentalRequestFarmerUserEmail(email)
            .stream()
            .map(payment -> new PaymentHistoryDto(
                    payment.getRentalRequest()
                           .getEquipment()
                           .getName(),          // equipmentName
                    payment.getAmount(),     // amount
                    payment.getStatus(),     // status
                    payment.getUpdatedAt()   // paidAt
            ))
            .toList();
}



}
