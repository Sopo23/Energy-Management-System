package com.example.device_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {
    private Long id;
    private String description;
    private String address;
    private float energyConsumption;
    private PersonDataDto personDataDto;
}
