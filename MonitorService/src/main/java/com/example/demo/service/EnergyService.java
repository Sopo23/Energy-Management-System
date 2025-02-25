package com.example.demo.service;

import com.example.demo.model.Device;
import com.example.demo.model.EnergyRecord;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.EnergyRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnergyService {

    private final DeviceRepository deviceRepository;
    private final EnergyRecordRepository energyRecordRepository;

    public EnergyService(DeviceRepository deviceRepository, EnergyRecordRepository energyRecordRepository) {
        this.deviceRepository = deviceRepository;
        this.energyRecordRepository = energyRecordRepository;
    }

    /**
     * Processes incoming energy data.
     *
     * @param deviceId          ID of the device sending the data.
     * @param energyConsumption Energy consumption value to process.
     */
    public void processEnergyData(String deviceId, double energyConsumption) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);

        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();

            // Save the energy consumption record
            EnergyRecord record = new EnergyRecord();
            record.setDeviceId(deviceId);
            record.setEnergyConsumption(energyConsumption);
            record.setTimestamp(LocalDateTime.now());
            energyRecordRepository.save(record);

            if (energyConsumption > device.getMhec()) {
                // Trigger notification (placeholder for WebSocket notifications)
                System.out.println("ALERT: Energy consumption exceeded for device " + deviceId);
            }
        } else {
            System.err.println("Device not found for deviceId: " + deviceId);
        }
    }
    public List<EnergyRecord> getRecordsForDeviceAndTimeRange(int deviceId, LocalDateTime start, LocalDateTime end) {
        return energyRecordRepository.findByDeviceIdAndTimestampBetween(String.valueOf(deviceId), start, end);
    }
}
