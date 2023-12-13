package com.example.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

public class History {

    Long id;

    @OneToOne
    Record record;

}
