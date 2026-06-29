package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.dto.PrescriptionRequest;
import com.medicare.smarthealthcare.entity.*;
import com.medicare.smarthealthcare.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionRepository prescriptionRepository;
    private final PatientProfileRepository patientRepository;
    private final DoctorProfileRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionController(PrescriptionRepository prescriptionRepository,
                                  PatientProfileRepository patientRepository,
                                  DoctorProfileRepository doctorRepository,
                                  AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public Prescription createPrescription(@AuthenticationPrincipal AppUser user,
                                           @Valid @RequestBody PrescriptionRequest request) {
        DoctorProfile doctor = doctorRepository.findByUser(user).orElseThrow();
        PatientProfile patient = patientRepository.findById(request.getPatientId()).orElseThrow();
        Prescription prescription = new Prescription();
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setMedicineName(request.getMedicineName());
        prescription.setDosage(request.getDosage());
        prescription.setInstructions(request.getInstructions());
        prescription.setStartDate(request.getStartDate());
        prescription.setEndDate(request.getEndDate());
        if (request.getAppointmentId() != null) {
            prescription.setAppointment(appointmentRepository.findById(request.getAppointmentId()).orElseThrow());
        }
        return prescriptionRepository.save(prescription);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<Prescription> getAll() {
        return prescriptionRepository.findAll();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    public List<Prescription> myPrescriptions(@AuthenticationPrincipal AppUser user) {
        if (user.getRole() == Role.PATIENT) {
            PatientProfile patient = patientRepository.findByUser(user).orElseThrow();
            return prescriptionRepository.findByPatient(patient);
        }
        DoctorProfile doctor = doctorRepository.findByUser(user).orElseThrow();
        return prescriptionRepository.findByDoctor(doctor);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<Prescription> prescriptionsByPatient(@PathVariable Long patientId) {
        PatientProfile patient = patientRepository.findById(patientId).orElseThrow();
        return prescriptionRepository.findByPatient(patient);
    }
}
