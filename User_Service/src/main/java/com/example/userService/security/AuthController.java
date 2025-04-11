package com.example.user_service.security;

import com.example.user_service.security.dto1.JwtResponse;
import com.example.user_service.security.dto1.LoginRequest;
import com.example.user_service.security.dto1.MessageResponse;

import com.example.user_service.security.dto1.SignupRequest;
import com.example.user_service.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


import static com.example.user_service.user.UrlMapping.AUTH;
import static com.example.user_service.user.UrlMapping.SIGN_IN;
import static com.example.user_service.user.UrlMapping.SIGN_UP;


@RequestMapping(AUTH)
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtUtilsService jwtUtilsService;

  @PostMapping(SIGN_IN)
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authService.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtilsService.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        JwtResponse.builder()
            .token(jwt)
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .roles(roles)
            .build());
  }

  @PostMapping(SIGN_UP)
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (authService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    authService.register(signUpRequest);


    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}