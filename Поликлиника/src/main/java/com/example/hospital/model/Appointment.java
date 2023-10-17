package com.example.hospital.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Appointment {


    int id;

    @NotNull
    String date_appointment;
    @NotNull
    String status;

    @NotNull
    @Valid
    Doctor doctor;

    @NotNull
    @Valid
    Patient patient;


}
