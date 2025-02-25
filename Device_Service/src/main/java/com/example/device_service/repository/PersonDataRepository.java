package com.example.device_service.repository;

import com.example.device_service.model.PersonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDataRepository extends JpaRepository<PersonData,Long> {
    Optional<PersonData> findByUserId(long userId);
    void delete (PersonData personData);

}
