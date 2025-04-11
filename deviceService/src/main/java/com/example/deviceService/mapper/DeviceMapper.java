package com.example.device_service.mapper;

import com.example.device_service.dto.DeviceDto;
import com.example.device_service.dto.UserReferenceDto;
import com.example.device_service.model.Device;
import com.example.device_service.model.UserReference;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceDto fromDeviceToDto(Device device) {
        if (device == null) {
            return null;
        }

        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setDescription(device.getDescription());
        deviceDto.setAddress(device.getAddress());
        deviceDto.setEnergyConsumption(device.getEnergyConsumption());

        if (device.getUserReference() != null) {
            deviceDto.setUserReferenceDto(fromUserReferenceToDto(device.getUserReference()));
        }

        return deviceDto;
    }

    public Device fromDtoToDevice(DeviceDto deviceDto) {
        if (deviceDto == null) {
            return null;
        }

        Device device = new Device();
        device.setId(deviceDto.getId());
        device.setDescription(deviceDto.getDescription());
        device.setAddress(deviceDto.getAddress());
        device.setEnergyConsumption(deviceDto.getEnergyConsumption());

        if (deviceDto.getUserReferenceDto() != null) {
            device.setUserReference(fromDtoToUserReference(deviceDto.getUserReferenceDto()));
        }

        return device;
    }

    public UserReferenceDto fromUserReferenceToDto(UserReference UserReference) {
        if (UserReference == null) {
            return null;
        }

        UserReferenceDto UserReferenceDto = new UserReferenceDto();
        UserReferenceDto.setId(UserReference.getId());
        UserReferenceDto.setUserId(UserReference.getUserId());
        UserReferenceDto.setDeviceIds(UserReference.getDeviceIds());

        return UserReferenceDto;
    }

    public UserReference fromDtoToUserReference(UserReferenceDto UserReferenceDto) {
        if (UserReferenceDto == null) {
            return null;
        }

        UserReference UserReference = new UserReference();
        UserReference.setId(UserReferenceDto.getId());
        UserReference.setUserId(UserReferenceDto.getUserId());
        UserReference.setDeviceIds(UserReferenceDto.getDeviceIds());

        return UserReference;
    }
}
