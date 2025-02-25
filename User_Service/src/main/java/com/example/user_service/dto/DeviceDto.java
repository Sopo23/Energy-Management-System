package com.example.user_service.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private Long id;
    private String description;
    private String address;
    private float energyConsumption;
}