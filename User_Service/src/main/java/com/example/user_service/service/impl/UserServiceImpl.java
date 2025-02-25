package com.example.user_service.service.impl;

import com.example.user_service.dto.UserDto;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.proxy.DeviceProxy;
import com.example.user_service.service.UserService;
import com.example.user_service.user.UserRepository;
import com.example.user_service.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final DeviceProxy deviceProxy;

    public UserServiceImpl(UserRepository userRepository,UserMapper userMapper,DeviceProxy deviceProxy) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.deviceProxy = deviceProxy;
    }

    // Retrieve all users
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users){
            userDtos.add(userMapper.fromUserToDto(user));
        }
        return userDtos;
    }

    // Retrieve a user by ID
    public Optional<UserDto> getUserById(long id) {
        return Optional.of(userMapper.fromUserToDto(userRepository.findById(id).get()));
    }

    // Update a user by ID
    public User updateUser(long id, UserDto userDto) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDto.getUsername());
            user.setRoles(userDto.getRoles());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // Delete a user by ID
    public void deleteUser(long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            userRepository.delete(user);
            deviceProxy.deletePersonDataByUserId(userId);  // Clean up device data related to the user
        });
    }


}
