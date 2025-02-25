package com.example.demo.repository;

import com.example.demo.model.EnergyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EnergyRecordRepository extends JpaRepository<EnergyRecord, Long> {
    List<EnergyRecord> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime start, LocalDateTime end);
}
