package com.example.device_service.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.device.updates.queue:device_changes}") // Default to 'device_changes'
    private String deviceUpdatesQueue;

    public DeviceEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Set the message converter to Jackson2JsonMessageConverter to ensure messages are sent as JSON
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void publishDeviceCreation(String deviceId, String description, String address, double mhec) {
        publishEvent("DEVICE_CREATED", deviceId, description, address, mhec);
    }

    public void publishDeviceUpdate(String deviceId, String description, String address, double mhec) {
        publishEvent("DEVICE_UPDATED", deviceId, description, address, mhec);
    }

    public void publishDeviceDeletion(String deviceId) {
        Map<String, Object> message = new HashMap<>();
        message.put("event", "DEVICE_DELETED");
        message.put("device_id", deviceId);

        System.out.println("Publishing DEVICE_DELETED event to queue: " + deviceUpdatesQueue);
        rabbitTemplate.convertAndSend("", deviceUpdatesQueue, message); // Send to default exchange
    }

    private void publishEvent(String eventType, String deviceId, String description, String address, double mhec) {
        Map<String, Object> message = new HashMap<>();
        message.put("event", eventType);
        message.put("device_id", deviceId);
        message.put("description", description);
        message.put("address", address);
        message.put("mhec", mhec);

        System.out.println("Publishing " + eventType + " event to queue: " + deviceUpdatesQueue);
        rabbitTemplate.convertAndSend("", deviceUpdatesQueue, message); // Send to default exchange
    }
}
