package com.example.user_service.dto;

import lombok.Data;

@Data
public class UserReferenceDto {

    private Long id;
    private int userId;
    private int[] deviceId;
}