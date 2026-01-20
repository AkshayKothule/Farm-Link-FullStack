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

    // One payment for one rental
    @OneToOne(optional = false)
    @JoinColumn(name = "rental_request_id", nullable = false, unique = true)
    private RentalRequest rentalRequest;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMode; // UPI, CASH, CARD

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED
}
