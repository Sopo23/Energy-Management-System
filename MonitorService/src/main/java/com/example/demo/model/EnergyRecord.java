package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "energyRecords")
public class EnergyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "deviceIds")
    private String deviceId;
    @Column(name = "energyConsumptions")
    private double energyConsumption;
    @Column(name = "timestamps")
    private LocalDateTime timestamp;

    public EnergyRecord() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
