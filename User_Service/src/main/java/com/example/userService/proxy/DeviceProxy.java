package com.example.user_service.proxy;

import com.example.user_service.dto.DeviceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "device-service", url = "${feign.client.config.device-service.url}")
public interface DeviceProxy {
        @GetMapping(path = "/devices/{id}")
        Optional<DeviceDto> getDeviceById(@PathVariable Long id);
        @DeleteMapping("/devices/person-data/{userId}")
        void deleteUserReferenceByUserId(@PathVariable("userId") long userId);
}
