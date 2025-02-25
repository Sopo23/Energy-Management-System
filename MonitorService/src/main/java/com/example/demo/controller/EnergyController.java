package com.example.demo.controller;

import com.example.demo.model.EnergyRecord;
import com.example.demo.proxy.DeviceProxy;
import com.example.demo.service.EnergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final EnergyService energyService;
    private final DeviceProxy deviceProxy;

    @Autowired
    public EnergyController(EnergyService energyService, DeviceProxy deviceProxy) {
        this.energyService = energyService;
        this.deviceProxy = deviceProxy;
    }

    @GetMapping("/user/{userId}/device/{deviceId}/consumption/{date}")
    public ResponseEntity<?> getHourlyEnergyConsumption(
            @PathVariable("userId") int userId,
            @PathVariable("deviceId") int deviceId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Check if the device belongs to the user
        List<Integer> userDevices = deviceProxy.getDeviceIdsForUser(userId);
        if (!userDevices.contains(deviceId)) {
            return ResponseEntity.status(403).body("Error: Device does not belong to the user.");
        }

        // Calculate MHEC for each hour and return as JSON
        List<Map<String, Object>> hourlyConsumption = calculateHourlyConsumption(deviceId, date);
        System.out.println("Hourly Consumption: " + hourlyConsumption);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(hourlyConsumption);
    }


    private List<Map<String, Object>> calculateHourlyConsumption(int deviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // Fetch energy records for the device on the given day
        List<EnergyRecord> records = energyService.getRecordsForDeviceAndTimeRange(deviceId, startOfDay, endOfDay);

        // Initialize list for hourly consumption
        List<Map<String, Object>> hourlyConsumptionList = new ArrayList<>();
        Map<Integer, Double> hourlyConsumption = new TreeMap<>();

        // Initialize hours with 0.0
        for (int hour = 0; hour < 24; hour++) {
            hourlyConsumption.put(hour, 0.0);
        }

        // Populate the consumption values
        for (EnergyRecord record : records) {
            int hour = record.getTimestamp().atZone(ZoneId.systemDefault()).getHour();
            hourlyConsumption.put(hour, hourlyConsumption.get(hour) + record.getEnergyConsumption());
        }

        // Convert the map into a list of key-value pairs
        for (Map.Entry<Integer, Double> entry : hourlyConsumption.entrySet()) {
            Map<String, Object> hourData = new LinkedHashMap<>();
            hourData.put("hour", entry.getKey());
            hourData.put("energyConsumption", entry.getValue());
            hourlyConsumptionList.add(hourData);
        }

        return hourlyConsumptionList;
    }

}
