package com.example.device_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDataDto {

    private Long id;
    private int userId;
    private List<Integer> deviceIds = new ArrayList<>();
}
