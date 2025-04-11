package com.example.user_service.security;

import com.example.user_service.security.dto1.SignupRequest;
import com.example.user_service.user.RoleRepository;
import com.example.user_service.user.UserRepository;
import com.example.user_service.user.model.ERole;
import com.example.user_service.user.model.Role;
import com.example.user_service.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }


  public void register(@Valid SignupRequest signUpRequest) {
    User user = User.builder()
        .username(signUpRequest.getUsername())
        .password(encoder.encode(signUpRequest.getPassword()))
        .build();

    Set<String> rolesStr = signUpRequest.getRoles();
    Set<Role> roles = new HashSet<>();

    if (rolesStr == null) {
      Role defaultRole = roleRepository.findByName(ERole.CUSTOMER)
          .orElseThrow(() -> new EntityNotFoundException("Cannot find CUSTOMER role"));
      roles.add(defaultRole);
    } else {
      rolesStr.forEach(r -> {
        Role ro = roleRepository.findByName(ERole.valueOf(r))
            .orElseThrow(() -> new EntityNotFoundException("Cannot find role: " + r));
        roles.add(ro);
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
  }

  public Authentication authenticate(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
    return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
  }
}