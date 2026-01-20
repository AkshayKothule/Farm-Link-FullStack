package com.farmlink.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "equipments",
    indexes = {
        @Index(name = "idx_equipment_available", columnList = "available")
    }
)
@Getter
@Setter
public class Equipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Double rentPerDay;

    @Column(nullable = false)
    private Boolean available = true;

    private String description;

    // ---- Image ----
    private String imageUrl;   // e.g. /images/tractor1.jpg
}

