package com.medicare.smarthealthcare.service;

import com.medicare.smarthealthcare.dto.AuthRequest;
import com.medicare.smarthealthcare.dto.AuthResponse;
import com.medicare.smarthealthcare.dto.RegisterRequest;
import com.medicare.smarthealthcare.entity.AppUser;
import com.medicare.smarthealthcare.entity.PatientProfile;
import com.medicare.smarthealthcare.entity.Role;
import com.medicare.smarthealthcare.repository.AppUserRepository;
import com.medicare.smarthealthcare.repository.PatientProfileRepository;
import com.medicare.smarthealthcare.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AppUserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(AppUserRepository userRepository,
                       PatientProfileRepository patientProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse registerPatient(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        AppUser user = new AppUser(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.PATIENT);
        AppUser savedUser = userRepository.save(user);

        PatientProfile profile = new PatientProfile(savedUser);
        profile.setPhone(request.getPhone());
        profile.setGender(request.getGender());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setAddress(request.getAddress());
        patientProfileRepository.save(profile);

        String token = jwtService.generateToken(savedUser);
        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AppUser user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
