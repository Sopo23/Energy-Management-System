package com.example.demo.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "device-service", url = "${feign.client.config.device-service.url}")
public interface DeviceProxy {

    @GetMapping("/devices/user/{userId}/devices")
    List<Integer> getDeviceIdsForUser(@PathVariable("userId") int userId);
}
