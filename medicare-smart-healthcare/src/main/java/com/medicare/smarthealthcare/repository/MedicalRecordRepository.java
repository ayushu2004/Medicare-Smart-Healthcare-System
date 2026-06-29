package com.medicare.smarthealthcare.repository;

import com.medicare.smarthealthcare.entity.DoctorProfile;
import com.medicare.smarthealthcare.entity.MedicalRecord;
import com.medicare.smarthealthcare.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatient(PatientProfile patient);
    List<MedicalRecord> findByDoctor(DoctorProfile doctor);
}
