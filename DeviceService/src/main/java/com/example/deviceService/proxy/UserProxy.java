package com.example.device_service.proxy;

import com.example.device_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserProxy {
    @GetMapping(path = "/users/{id}")
    Optional<UserDto> getUserById(@PathVariable Long id);
}

