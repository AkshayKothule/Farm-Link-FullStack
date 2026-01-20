package com.farmlink.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String addressLine;

    private String village;
    private String taluka;
    private String district;
    private String state;

    @Column(nullable = false)
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
