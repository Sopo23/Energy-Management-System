package com.example.user_service.security.dto1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
}