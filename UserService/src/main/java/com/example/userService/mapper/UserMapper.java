package com.example.user_service.mapper;

import com.example.user_service.dto.UserDto;
import com.example.user_service.user.model.User;
import com.example.user_service.user.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles")
    User fromDtoToUser(UserDto userDto);

    @Mapping(target = "roles", source = "roles")
    UserDto fromUserToDto(User user);

    default Role roleFromDto(Role roleDto) {
        if (roleDto == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setName(roleDto.getName());
        return role;
    }

    default Role roleToDto(Role role) {
        if (role == null) {
            return null;
        }
        Role roleDto = new Role();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }
}
