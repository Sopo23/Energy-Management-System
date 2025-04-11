package com.example.device_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "descriptions")
    private String description;
    @Column(name = "addresses")
    private String address;
    @Column(name = "energyConsumptions")
    private float energyConsumption;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id")
    private UserReference UserReference;
}
