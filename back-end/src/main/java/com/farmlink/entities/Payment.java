package com.farmlink.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "rental_request_id", nullable = false, unique = true)
    private RentalRequest rentalRequest;

    @Column(nullable = false)
    private Double amount;

    // Razorpay fields
    @Column(nullable = false, unique = true)
    private String razorpayOrderId;

    private String razorpayPaymentId;
    private String razorpaySignature;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // CREATED, SUCCESS, FAILED
}
