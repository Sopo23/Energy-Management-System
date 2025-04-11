package com.example.demo.service;

import com.example.demo.model.Device;
import com.example.demo.model.EnergyRecord;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.EnergyRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class EnergyService implements EnergyServiceInterface {

    private final DeviceRepository deviceRepository;
    private final EnergyRecordRepository energyRecordRepository;

    public EnergyService(DeviceRepository deviceRepository, EnergyRecordRepository energyRecordRepository) {
        this.deviceRepository = deviceRepository;
        this.energyRecordRepository = energyRecordRepository;
    }

    @Override
    public void processEnergyData(String deviceId, double energyConsumption) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);

        if (optionalDevice.isPresent()) {
            Device device = optionalDevice.get();

            EnergyRecord record = new EnergyRecord();
            record.setDeviceId(deviceId);
            record.setEnergyConsumption(energyConsumption);
            record.setTimestamp(LocalDateTime.now());
            energyRecordRepository.save(record);

            if (energyConsumption > device.getMhec()) {
                System.out.println("ALERT: Energy consumption exceeded for device " + deviceId);
            }
        } else {
            System.err.println("Device not found for deviceId: " + deviceId);
        }
    }

    @Override
    public List<EnergyRecord> getRecordsForDeviceAndTimeRange(int deviceId, LocalDateTime start, LocalDateTime end) {
        return energyRecordRepository.findByDeviceIdAndTimestampBetween(String.valueOf(deviceId), start, end);
    }

    @Override
    public List<Map<String, Object>> calculateHourlyConsumption(int deviceId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<EnergyRecord> records = getRecordsForDeviceAndTimeRange(deviceId, startOfDay, endOfDay);

        Map<Integer, Double> hourlyConsumption = new TreeMap<>();
        for (int hour = 0; hour < 24; hour++) {
            hourlyConsumption.put(hour, 0.0);
        }

        for (EnergyRecord record : records) {
            int hour = record.getTimestamp().atZone(ZoneId.systemDefault()).getHour();
            hourlyConsumption.put(hour, hourlyConsumption.get(hour) + record.getEnergyConsumption());
        }

        List<Map<String, Object>> hourlyConsumptionList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : hourlyConsumption.entrySet()) {
            Map<String, Object> hourData = new LinkedHashMap<>();
            hourData.put("hour", entry.getKey());
            hourData.put("energyConsumption", entry.getValue());
            hourlyConsumptionList.add(hourData);
        }

        return hourlyConsumptionList;
    }
}
