package com.example.demo.listener;

import com.example.demo.model.Device;
import com.example.demo.service.DeviceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeviceUpdatesListener {

    private final DeviceService deviceService;

    public DeviceUpdatesListener(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RabbitListener(queues = "device_changes")
    public void handleDeviceUpdates(@Payload Map<String, Object> eventData) {
        String eventType = (String) eventData.get("event");
        System.out.println("Received event: " + eventType);

        switch (eventType) {
            case "DEVICE_CREATED":
            case "DEVICE_UPDATED":
                handleCreateOrUpdate(eventData);
                break;

            case "DEVICE_DELETED":
                handleDelete(eventData);
                break;

            default:
                System.err.println("Unknown event type: " + eventType);
        }
    }

    private void handleCreateOrUpdate(Map<String, Object> eventData) {
        String deviceId = (String) eventData.get("device_id");
        String description = (String) eventData.get("description");
        String address = (String) eventData.get("address");
        double mhec = Double.parseDouble(eventData.get("mhec").toString());

        Device device = deviceService.getDeviceById(deviceId).orElse(new Device());
        device.setDeviceId(deviceId);
        device.setDescription(description);
        device.setAddress(address);
        device.setMhec(mhec);

        deviceService.saveOrUpdateDevice(device);
        System.out.println("Processed " + eventData.get("event") + " for device: " + deviceId);
    }

    private void handleDelete(Map<String, Object> eventData) {
        String deviceId = (String) eventData.get("device_id");
        deviceService.deleteDevice(deviceId);
        System.out.println("Processed DEVICE_DELETED for device: " + deviceId);
    }
}
