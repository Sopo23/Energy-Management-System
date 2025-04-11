package com.example.device_service.repository;

import com.example.device_service.model.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReferenceRepository extends JpaRepository<UserReference,Long> {
    Optional<UserReference> findByUserId(long userId);
    void delete (UserReference UserReference);

}
