package com.example.hospital.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,PATIENT,DOCTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
