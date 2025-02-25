package com.example.user_service.security;

import com.example.user_service.user.UserDetailsImplService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtUtilsService jwtUtilsService;
  private final UserDetailsImplService userDetailsService;

  public AuthTokenFilter(JwtUtilsService jwtUtilsService, UserDetailsImplService userDetailsService) {
    this.jwtUtilsService = jwtUtilsService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    try {
      String jwt = parseJwt(request);

      if (jwt != null && jwtUtilsService.validateJwtToken(jwt)) {
        String userId = jwtUtilsService.getUserIdFromJwtToken(jwt); // Update method to extract user ID
        UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(userId)); // Load by ID

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      System.out.println("Cannot set user authentication: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
  }



  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}