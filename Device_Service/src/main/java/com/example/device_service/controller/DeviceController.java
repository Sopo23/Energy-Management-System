package com.example.device_service.controller;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.PersonDataDto;
import com.example.device_service.service.impl.DeviceEventPublisher;
import com.example.device_service.service.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceServiceImpl deviceService;
    private final DeviceEventPublisher eventPublisher;

    @Autowired
    public DeviceController(DeviceServiceImpl deviceService, DeviceEventPublisher eventPublisher) {
        this.deviceService = deviceService;
        this.eventPublisher = eventPublisher; // Inject the publisher
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto) {
        DeviceDto createdDevice = deviceService.createDevice(deviceDto);
        System.out.println("published");
        eventPublisher.publishDeviceCreation(
                createdDevice.getId().toString(),
                createdDevice.getDescription(),
                createdDevice.getAddress(), // Address added here
                createdDevice.getEnergyConsumption()
        );
        return ResponseEntity.ok(createdDevice);
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        List<DeviceDto> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        Optional<DeviceDto> device = deviceService.getDeviceById(id);
        return device.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long id, @RequestBody DeviceDto deviceDto) {
        try {
            DeviceDto updatedDeviceDto = deviceService.updateDevice(id, deviceDto);
            eventPublisher.publishDeviceUpdate(
                    updatedDeviceDto.getId().toString(),
                    updatedDeviceDto.getDescription(),
                    updatedDeviceDto.getAddress(), // Address added here
                    updatedDeviceDto.getEnergyConsumption()
            );
            return ResponseEntity.ok(updatedDeviceDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/{deviceId}")
    public ResponseEntity<PersonDataDto> addDeviceToUser(@PathVariable int userId, @PathVariable Long deviceId) {
        return ResponseEntity.ok().body(deviceService.addDeviceToUser(userId, deviceId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        try {
            deviceService.deleteDevice(id);
            eventPublisher.publishDeviceDeletion(id.toString());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/person-data/{userId}")
    public ResponseEntity<Void> deletePersonDataByUserId(@PathVariable("userId") int userId) {
        deviceService.deletePersonDataByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/devices")
    public ResponseEntity<List<Integer>> getDeviceIdsForUser(@PathVariable("userId") int userId) {
        List<Integer> deviceIds = deviceService.getDeviceIdsForUser(userId);
        return ResponseEntity.ok(deviceIds);
    }
}
