package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.Appointment;
import com.medicare.smarthealthcare.entity.DoctorProfile;
import com.medicare.smarthealthcare.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(PatientProfile patient);
    List<Appointment> findByDoctor(DoctorProfile doctor);
}
