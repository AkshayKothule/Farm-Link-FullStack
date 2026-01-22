package com.farmlink.entities;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "rental_requests",
    indexes = {
        @Index(name = "idx_rental_equipment_status", columnList = "equipment_id, status"),
        @Index(name = "idx_rental_dates", columnList = "start_date, end_date")
    }
)
@Getter
@Setter
public class RentalRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Farmer who is renting
    @ManyToOne(optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    // Equipment being rented
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    
    @Column(nullable = false)
    private Double totalAmount; 

    // Rental period
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // Request status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;
    
    
    
}

