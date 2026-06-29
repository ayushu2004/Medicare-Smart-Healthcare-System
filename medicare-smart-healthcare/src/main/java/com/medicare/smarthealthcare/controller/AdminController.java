package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.dto.CreateUserRequest;
import com.medicare.smarthealthcare.dto.UserResponse;
import com.medicare.smarthealthcare.entity.*;
import com.medicare.smarthealthcare.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AppUserRepository userRepository;
    private final DoctorProfileRepository doctorRepository;
    private final PatientProfileRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(AppUserRepository userRepository,
                           DoctorProfileRepository doctorRepository,
                           PatientProfileRepository patientRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).toList();
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        AppUser user = new AppUser(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getRole());
        AppUser saved = userRepository.save(user);

        if (request.getRole() == Role.DOCTOR) {
            DoctorProfile doctor = new DoctorProfile(saved);
            doctor.setPhone(request.getPhone());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setLicenseNumber(request.getLicenseNumber());
            doctor.setAvailability(request.getAvailability());
            doctorRepository.save(doctor);
        } else if (request.getRole() == Role.PATIENT) {
            PatientProfile patient = new PatientProfile(saved);
            patient.setPhone(request.getPhone());
            patient.setGender(request.getGender());
            patient.setBloodGroup(request.getBloodGroup());
            patient.setAddress(request.getAddress());
            patientRepository.save(patient);
        }
        return ResponseEntity.ok(new UserResponse(saved));
    }

    @PatchMapping("/users/{id}/enabled")
    public UserResponse setEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        AppUser user = userRepository.findById(id).orElseThrow();
        user.setEnabled(enabled);
        return new UserResponse(userRepository.save(user));
    }
}
