package com.example.user_service.user;

import com.example.user_service.user.model.ERole;
import com.example.user_service.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole role);

}