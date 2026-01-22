package com.farmlink.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.farmlink.customexception.ResourceNotFoundException;
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
            Long rentalId, String email) {

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

        // Prevent duplicate payment
        if (paymentRepository.existsByRentalRequestId(rentalId)) {
            throw new IllegalStateException(
                    "Payment already initiated");
        }

        double amount = rental.getTotalAmount();

        try {
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

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }

    // STEP 2: VERIFY PAYMENT
    @Override
    public void verifyPayment(PaymentVerifyRequestDto dto) {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(dto.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        payment.setRazorpayPaymentId(dto.getPaymentId());
        payment.setRazorpaySignature(dto.getSignature());
        payment.setStatus(PaymentStatus.SUCCESS);

        // Complete rental
        payment.getRentalRequest()
               .setStatus(RentalStatus.COMPLETED);

        paymentRepository.save(payment);
    }
}
