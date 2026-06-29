package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    Optional<PatientProfile> findByUser(AppUser user);
    Optional<PatientProfile> findByUserId(Long userId);
}
