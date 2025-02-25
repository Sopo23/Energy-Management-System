package com.example.device_service.service.impl;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.PersonDataDto;
import com.example.device_service.mapper.DeviceMapper;
import com.example.device_service.model.Device;
import com.example.device_service.model.PersonData;
import com.example.device_service.repository.DeviceRepository;
import com.example.device_service.repository.PersonDataRepository;
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
    private final PersonDataRepository personDataRepository;
    public DeviceServiceImpl(DeviceRepository deviceRepository,DeviceMapper deviceMapper, PersonDataRepository personDataRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.personDataRepository = personDataRepository;
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


    public PersonDataDto addDeviceToUser(int userId, Long deviceId) {
        PersonData personData = personDataRepository.findByUserId(userId)
                .orElseGet(() -> {
                    PersonData newPersonData = new PersonData(null, userId, new ArrayList<>());
                    personDataRepository.save(newPersonData);
                    return newPersonData;
                });

        deviceRepository.findById(deviceId).ifPresent(device -> {
            int deviceIdAsInt = device.getId().intValue();
            if (!personData.getDeviceIds().contains(deviceIdAsInt)) {
                personData.getDeviceIds().add(deviceIdAsInt);
                personDataRepository.save(personData);
            }
        });
        return deviceMapper.fromPersonDataToDto(personData);
    }


    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public void deletePersonDataByUserId(int userId) {
        Optional<PersonData> personDataOptional = personDataRepository.findByUserId(userId);

        if (personDataOptional.isPresent()) {
            PersonData personData = personDataOptional.get();

            personData.getDeviceIds().clear();
            personDataRepository.save(personData);
            personDataRepository.delete(personData);
        }
    }
    public List<Integer> getDeviceIdsForUser(int userId) {
        Optional<PersonData> personDataOptional = personDataRepository.findByUserId(userId);
        if (personDataOptional.isPresent()) {
            return personDataOptional.get().getDeviceIds();
        } else {
            return new ArrayList<>();
        }
    }

}
