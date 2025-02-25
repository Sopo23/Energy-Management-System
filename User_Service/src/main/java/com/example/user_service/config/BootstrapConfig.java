package com.example.user_service.config;

import com.example.user_service.security.AuthService;
import com.example.user_service.security.dto1.SignupRequest;
import com.example.user_service.user.RoleRepository;
import com.example.user_service.user.UserRepository;
import com.example.user_service.user.model.ERole;
import com.example.user_service.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class BootstrapConfig {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private String adminUsername = new String("admin");

    private String adminPassword = new String("1234");

    private final RoleRepository roleRepository;
    private final UserRepository appUserRepository;
    private final AuthService authService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        // Check if database schema is set to create, indicating a fresh setup
        if ("create".equalsIgnoreCase(ddlAuto) || "create-drop".equalsIgnoreCase(ddlAuto)) {
            // Create all roles
            for (ERole role : ERole.values()) {
                roleRepository.findByName(role)
                        .orElseGet(() -> roleRepository.save( Role.builder()
                                .name(role)
                                .build()));
            }

            // Create a default admin user if it does not exist
            HashSet<String> set = new HashSet<>();
            set.add("ADMIN");
            appUserRepository.findByUsername(adminUsername).orElseGet(() -> {
                SignupRequest request = SignupRequest.builder()
                        .username(adminUsername)
                        .password(adminPassword)
                        .roles(set)
                        .build();
                authService.register(request);
                return appUserRepository.findByUsername(adminUsername).orElseThrow();
            });
        }
    }
}
