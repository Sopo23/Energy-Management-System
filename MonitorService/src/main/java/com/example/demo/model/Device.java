package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    private String deviceId;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "mhec")
    private double mhec;

    public Device() {}

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMhec() {
        return mhec;
    }

    public void setMhec(double mhec) {
        this.mhec = mhec;
    }
}
