package com.example.demo.service;

import com.example.demo.model.Device;
import com.example.demo.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Saves or updates a device in the database.
     *
     * @param device Device object to save or update.
     */
    public void saveOrUpdateDevice(Device device) {
        deviceRepository.save(device);
    }

    /**
     * Deletes a device by its ID.
     *
     * @param deviceId The ID of the device.
     */
    public void deleteDevice(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    /**
     * Fetches a device by its ID.
     *
     * @param deviceId The ID of the device.
     * @return The device, if found.
     */
    public Optional<Device> getDeviceById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }
}
