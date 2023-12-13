package com.example.hospital.service;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@Scope("prototype")
public class PasswordGenerator {

    private final SecureRandom secureRandom;
    private final PasswordEncoder passwordEncoder;

    public PasswordGenerator() {
        this.passwordEncoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
        secureRandom = new SecureRandom();
    }

    public String generateRandomPassword() {
        byte[] randomBytes = new byte[7];
        secureRandom.nextBytes(randomBytes);
        String randomPassword = Base64.getEncoder().encodeToString(randomBytes);
        return passwordEncoder.encode(randomPassword);
    }
}
