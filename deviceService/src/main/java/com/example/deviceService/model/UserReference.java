package com.example.device_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "usersReference")
public class UserReference {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "userId", unique = true, nullable = false)
    private int userId;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "person_devices", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "device_id")
    private List<Integer> deviceIds = new ArrayList<>();
}
