package com.example.device_service.service;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.UserReferenceDto;
import com.example.device_service.model.Device;
import com.example.device_service.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface DeviceService {

    DeviceDto createDevice(DeviceDto deviceDto);

    List<DeviceDto> getAllDevices();

    Optional<DeviceDto> getDeviceById(Long id);

    DeviceDto updateDevice(Long id, DeviceDto deviceDetails);

    UserReferenceDto addDeviceToUser(int userId, Long deviceId);

    void deleteDevice(Long id);

    void deleteUserReferenceByUserId(int userId);
    List<Integer> getDeviceIdsForUser(int userId);

}
