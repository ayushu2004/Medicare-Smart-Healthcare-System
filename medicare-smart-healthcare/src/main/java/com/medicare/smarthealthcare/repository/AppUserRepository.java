package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<AppUser> findByRole(Role role);
}
