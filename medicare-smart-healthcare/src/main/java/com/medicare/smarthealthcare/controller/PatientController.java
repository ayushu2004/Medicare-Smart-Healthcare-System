package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.PatientProfile;
import com.medicare.smarthealthcare.repository.PatientProfileRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientProfileRepository patientRepository;

    public PatientController(PatientProfileRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public List<PatientProfile> getPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientProfile myProfile(@AuthenticationPrincipal AppUser user) {
        return patientRepository.findByUser(user).orElseThrow();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public PatientProfile getPatient(@PathVariable Long id) {
        return patientRepository.findById(id).orElseThrow();
    }
}
