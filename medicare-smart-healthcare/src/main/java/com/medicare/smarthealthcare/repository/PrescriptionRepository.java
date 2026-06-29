package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.DoctorProfile;
import com.medicare.smarthealthcare.entity.PatientProfile;
import com.medicare.smarthealthcare.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient(PatientProfile patient);
    List<Prescription> findByDoctor(DoctorProfile doctor);
}
