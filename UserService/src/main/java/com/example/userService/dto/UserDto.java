package com.example.user_service.dto;

import com.example.user_service.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();
}
