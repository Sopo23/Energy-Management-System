package com.example.device_service.service.impl;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.UserReferenceDto;
import com.example.device_service.mapper.DeviceMapper;
import com.example.device_service.model.Device;
import com.example.device_service.model.UserReference;
import com.example.device_service.repository.DeviceRepository;
import com.example.device_service.repository.UserReferenceRepository;
import com.example.device_service.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final UserReferenceRepository UserReferenceRepository;
    public DeviceServiceImpl(DeviceRepository deviceRepository,DeviceMapper deviceMapper, UserReferenceRepository UserReferenceRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.UserReferenceRepository = UserReferenceRepository;
    }

    public DeviceDto createDevice(DeviceDto deviceDto) {
        System.out.println("Creating device with data: " + deviceDto);

        Device device = deviceMapper.fromDtoToDevice(deviceDto);

        System.out.println("Mapped device entity: " + device);

        Device savedDevice = deviceRepository.save(device);

        return deviceMapper.fromDeviceToDto(savedDevice);
    }

    public List<DeviceDto> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        List<DeviceDto> deviceDtos = new ArrayList<>();
        for(Device device : devices){
            deviceDtos.add(deviceMapper.fromDeviceToDto(device));
        }
        return deviceDtos;
    }

    public Optional<DeviceDto> getDeviceById(Long id) {
        return Optional.of(deviceMapper.fromDeviceToDto(deviceRepository.findById(id).get()));
    }

    public DeviceDto updateDevice(Long id, DeviceDto deviceDetails) {
        return deviceRepository.findById(id).map(device -> {
            device.setDescription(deviceDetails.getDescription());
            device.setAddress(deviceDetails.getAddress());
            device.setEnergyConsumption(deviceDetails.getEnergyConsumption());
            deviceRepository.save(device);
            return deviceMapper.fromDeviceToDto(device);
        }).orElseThrow(() -> new RuntimeException("Device not found with id " + id));
    }


    public UserReferenceDto addDeviceToUser(int userId, Long deviceId) {
        UserReference UserReference = UserReferenceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserReference newUserReference = new UserReference(null, userId, new ArrayList<>());
                    UserReferenceRepository.save(newUserReference);
                    return newUserReference;
                });

        deviceRepository.findById(deviceId).ifPresent(device -> {
            int deviceIdAsInt = device.getId().intValue();
            if (!UserReference.getDeviceIds().contains(deviceIdAsInt)) {
                UserReference.getDeviceIds().add(deviceIdAsInt);
                UserReferenceRepository.save(UserReference);
            }
        });
        return deviceMapper.fromUserReferenceToDto(UserReference);
    }


    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public void deleteUserReferenceByUserId(int userId) {
        Optional<UserReference> UserReferenceOptional = UserReferenceRepository.findByUserId(userId);

        if (UserReferenceOptional.isPresent()) {
            UserReference UserReference = UserReferenceOptional.get();

            UserReference.getDeviceIds().clear();
            UserReferenceRepository.save(UserReference);
            UserReferenceRepository.delete(UserReference);
        }
    }
    public List<Integer> getDeviceIdsForUser(int userId) {
        Optional<UserReference> UserReferenceOptional = UserReferenceRepository.findByUserId(userId);
        if (UserReferenceOptional.isPresent()) {
            return UserReferenceOptional.get().getDeviceIds();
        } else {
            return new ArrayList<>();
        }
    }

}
