package com.example.user_service.service;

import com.example.user_service.dto.UserDto;
import com.example.user_service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(long id);

    User updateUser(long id, UserDto userDetails);

    void deleteUser(long id);
}
