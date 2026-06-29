package com.medicare.smarthealthcare.controller;

import com.medicare.smarthealthcare.dto.AppointmentRequest;
import com.medicare.smarthealthcare.entity.*;
import com.medicare.smarthealthcare.repository.AppointmentRepository;
import com.medicare.smarthealthcare.repository.DoctorProfileRepository;
import com.medicare.smarthealthcare.repository.PatientProfileRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientRepository;
    private final DoctorProfileRepository doctorRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 PatientProfileRepository patientRepository,
                                 DoctorProfileRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public Appointment bookAppointment(@AuthenticationPrincipal AppUser user,
                                       @Valid @RequestBody AppointmentRequest request) {
        PatientProfile patient = patientRepository.findByUser(user).orElseThrow();
        DoctorProfile doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow();
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        return appointmentRepository.save(appointment);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public List<Appointment> allAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR')")
    public List<Appointment> myAppointments(@AuthenticationPrincipal AppUser user) {
        if (user.getRole() == Role.PATIENT) {
            PatientProfile patient = patientRepository.findByUser(user).orElseThrow();
            return appointmentRepository.findByPatient(patient);
        }
        DoctorProfile doctor = doctorRepository.findByUser(user).orElseThrow();
        return appointmentRepository.findByDoctor(doctor);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','RECEPTIONIST')")
    public Appointment updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}
