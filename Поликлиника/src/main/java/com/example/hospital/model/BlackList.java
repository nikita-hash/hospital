package com.example.hospital.model;

import jakarta.persistence.*;

@Entity
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String date_start;
    String term;
    String reason;

    @OneToOne
    User account;
}
