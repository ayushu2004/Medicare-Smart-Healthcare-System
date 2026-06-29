package com.medicare.smarthealthcare.config;

import com.medicare.smarthealthcare.entity.*;
import com.medicare.smarthealthcare.repository.AppUserRepository;
import com.medicare.smarthealthcare.repository.DoctorProfileRepository;
import com.medicare.smarthealthcare.repository.PatientProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedDemoUsers(AppUserRepository userRepository,
                                    DoctorProfileRepository doctorRepository,
                                    PatientProfileRepository patientRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@medicare.com")) {
                userRepository.save(new AppUser("System Admin", "admin@medicare.com", passwordEncoder.encode("Admin@123"), Role.ADMIN));
            }
            if (!userRepository.existsByEmail("doctor@medicare.com")) {
                AppUser doctorUser = userRepository.save(new AppUser("Dr. Asha Sharma", "doctor@medicare.com", passwordEncoder.encode("Doctor@123"), Role.DOCTOR));
                DoctorProfile doctor = new DoctorProfile(doctorUser);
                doctor.setSpecialization("General Medicine");
                doctor.setLicenseNumber("MED-DOC-1001");
                doctor.setPhone("9876543210");
                doctor.setAvailability("Mon-Fri 10:00-16:00");
                doctorRepository.save(doctor);
            }
            if (!userRepository.existsByEmail("patient@medicare.com")) {
                AppUser patientUser = userRepository.save(new AppUser("Rahul Patient", "patient@medicare.com", passwordEncoder.encode("Patient@123"), Role.PATIENT));
                PatientProfile patient = new PatientProfile(patientUser);
                patient.setPhone("9123456780");
                patient.setGender("Male");
                patient.setBloodGroup("O+");
                patient.setAddress("Bengaluru, India");
                patientRepository.save(patient);
            }
            if (!userRepository.existsByEmail("reception@medicare.com")) {
                userRepository.save(new AppUser("Reception Desk", "reception@medicare.com", passwordEncoder.encode("Reception@123"), Role.RECEPTIONIST));
            }
        };
    }
}
