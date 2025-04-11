package com.example.demo.service;

import com.example.demo.model.EnergyRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EnergyServiceInterface {

    void processEnergyData(String deviceId, double energyConsumption);

    List<EnergyRecord> getRecordsForDeviceAndTimeRange(int deviceId, LocalDateTime start, LocalDateTime end);

    List<Map<String, Object>> calculateHourlyConsumption(int deviceId, LocalDate date);
}
