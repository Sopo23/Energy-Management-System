package com.example.device_service.mapper;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.PersonDataDto;
import com.example.device_service.model.Device;
import com.example.device_service.model.PersonData;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    // Map Device entity to DeviceDto
    public DeviceDto fromDeviceToDto(Device device) {
        if (device == null) {
            return null;
        }

        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setDescription(device.getDescription());
        deviceDto.setAddress(device.getAddress());
        deviceDto.setEnergyConsumption(device.getEnergyConsumption());

        if (device.getPersonData() != null) {
            deviceDto.setPersonDataDto(fromPersonDataToDto(device.getPersonData()));
        }

        return deviceDto;
    }

    // Map DeviceDto to Device entity
    public Device fromDtoToDevice(DeviceDto deviceDto) {
        if (deviceDto == null) {
            return null;
        }

        Device device = new Device();
        device.setId(deviceDto.getId());
        device.setDescription(deviceDto.getDescription());
        device.setAddress(deviceDto.getAddress());
        device.setEnergyConsumption(deviceDto.getEnergyConsumption());

        if (deviceDto.getPersonDataDto() != null) {
            device.setPersonData(fromDtoToPersonData(deviceDto.getPersonDataDto()));
        }

        return device;
    }

    // Helper method to map PersonData to PersonDataDto
    public PersonDataDto fromPersonDataToDto(PersonData personData) {
        if (personData == null) {
            return null;
        }

        PersonDataDto personDataDto = new PersonDataDto();
        personDataDto.setId(personData.getId());
        personDataDto.setUserId(personData.getUserId());
        personDataDto.setDeviceIds(personData.getDeviceIds());

        return personDataDto;
    }

    // Helper method to map PersonDataDto to PersonData
    public PersonData fromDtoToPersonData(PersonDataDto personDataDto) {
        if (personDataDto == null) {
            return null;
        }

        PersonData personData = new PersonData();
        personData.setId(personDataDto.getId());
        personData.setUserId(personDataDto.getUserId());
        personData.setDeviceIds(personDataDto.getDeviceIds());

        return personData;
    }
}
