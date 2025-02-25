package com.example.user_service.user;

import com.example.user_service.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service

public class UserDetailsImplService implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsImplService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));

    return UserDetailsImpl.build(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }
}