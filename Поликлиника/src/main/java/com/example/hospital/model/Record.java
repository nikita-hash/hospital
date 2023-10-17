package com.example.hospital.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.Date;

public class Record {
    Long id;
    Date date;

    @OneToMany
    Doctor doctor;

    @OneToMany
    User user;

    @Enumerated(value = EnumType.STRING)
    RecordStatus recordStatus;
}
