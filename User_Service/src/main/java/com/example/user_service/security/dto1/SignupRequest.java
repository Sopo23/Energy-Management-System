package com.example.user_service.security.dto1;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SignupRequest {
  private String username;
  private String password;
  private Set<String> roles;
}

/*
{
  username: "blabla",
  password: "123123123",
  email: "alex@email.com",
  roles: [MANAGER,CUSTOMER]
}
 */