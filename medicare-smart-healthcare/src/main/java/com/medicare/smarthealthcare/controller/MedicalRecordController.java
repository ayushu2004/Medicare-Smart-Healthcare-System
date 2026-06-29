package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.dto.MedicalRecordRequest;
import com.medicare.smarthealthcare.entity.*;
import com.medicare.smarthealthcare.repository.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientProfileRepository patientRepository;
    private final DoctorProfileRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository,
                                   PatientProfileRepository patientRepository,
                                   DoctorProfileRepository doctorRepository,
                                   AppointmentRepository appointmentRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public MedicalRecord createRecord(@AuthenticationPrincipal AppUser user,
                                      @Valid @RequestBody MedicalRecordRequest request) {
        DoctorProfile doctor = doctorRepository.findByUser(user).orElseThrow();
        PatientProfile patient = patientRepository.findById(request.getPatientId()).orElseThrow();
        MedicalRecord record = new MedicalRecord();
        record.setDoctor(doctor);
        record.setPatient(patient);
        record.setSymptoms(request.getSymptoms());
        record.setDiagnosis(request.getDiagnosis());
        record.setNotes(request.getNotes());
        if (request.getAppointmentId() != null) {
            record.setAppointment(appointmentRepository.findById(request.getAppointmentId()).orElseThrow());
        }
        return medicalRecordRepository.save(record);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<MedicalRecord> getAllRecords() {
        return medicalRecordRepository.findAll();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    public List<MedicalRecord> myRecords(@AuthenticationPrincipal AppUser user) {
        if (user.getRole() == Role.PATIENT) {
            PatientProfile patient = patientRepository.findByUser(user).orElseThrow();
            return medicalRecordRepository.findByPatient(patient);
        }
        DoctorProfile doctor = doctorRepository.findByUser(user).orElseThrow();
        return medicalRecordRepository.findByDoctor(doctor);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public List<MedicalRecord> recordsByPatient(@PathVariable Long patientId) {
        PatientProfile patient = patientRepository.findById(patientId).orElseThrow();
        return medicalRecordRepository.findByPatient(patient);
    }
}
