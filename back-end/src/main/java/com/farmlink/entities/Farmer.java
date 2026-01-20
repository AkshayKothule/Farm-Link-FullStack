package com.farmlink.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "farmers")
@Getter
@Setter
public class Farmer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to base user
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Type of farming
    @Column(nullable = false)
    private String farmType; // Crop / Dairy / Mixed

    // Land size (in acres)
    private Double landArea;


}
