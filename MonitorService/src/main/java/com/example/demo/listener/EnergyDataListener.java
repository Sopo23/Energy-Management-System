package com.example.demo.listener;

import com.example.demo.service.EnergyService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EnergyDataListener {

    private final EnergyService energyService;

    public EnergyDataListener(EnergyService energyService) {
        this.energyService = energyService;
    }

    @RabbitListener(queues = "energy_queue")
    public void handleEnergyData(Map<String, Object> message) {
        try {
            String deviceId = (String) message.get("device_id");
            double measurementValue = Double.parseDouble(message.get("measurement_value").toString());

            // Process the energy data
            energyService.processEnergyData(deviceId, measurementValue);
            System.out.println("Processed energy data: " + deviceId + " = " + measurementValue);
        } catch (Exception e) {
            System.err.println("Failed to process energy data message: " + message);
            e.printStackTrace();
        }
    }
}
