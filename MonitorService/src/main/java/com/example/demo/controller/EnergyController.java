package com.example.demo.controller;

import com.example.demo.proxy.DeviceProxy;
import com.example.demo.service.EnergyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    private final EnergyService energyService;
    private final DeviceProxy deviceProxy;

    public EnergyController(EnergyService energyService, DeviceProxy deviceProxy) {
        this.energyService = energyService;
        this.deviceProxy = deviceProxy;
    }

    @GetMapping("/user/{userId}/device/{deviceId}/consumption/{date}")
    public ResponseEntity<?> getHourlyEnergyConsumption(
            @PathVariable("userId") int userId,
            @PathVariable("deviceId") int deviceId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Integer> userDevices = deviceProxy.getDeviceIdsForUser(userId);
        if (!userDevices.contains(deviceId)) {
            return ResponseEntity.status(403).body("Error: Device does not belong to the user.");
        }

        List<Map<String, Object>> hourlyConsumption = energyService.calculateHourlyConsumption(deviceId, date);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(hourlyConsumption);
    }
}
