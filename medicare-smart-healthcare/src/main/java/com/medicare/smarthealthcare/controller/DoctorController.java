package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.entity.DoctorProfile;
import com.medicare.smarthealthcare.repository.DoctorProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorProfileRepository doctorRepository;

    public DoctorController(DoctorProfileRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public List<DoctorProfile> getDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    public DoctorProfile getDoctor(@PathVariable Long id) {
        return doctorRepository.findById(id).orElseThrow();
    }
}
