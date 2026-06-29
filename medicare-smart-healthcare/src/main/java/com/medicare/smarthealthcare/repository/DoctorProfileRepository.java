package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUser(AppUser user);
    Optional<DoctorProfile> findByUserId(Long userId);
}
