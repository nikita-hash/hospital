package com.example.hospital.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "doctor")
@PrimaryKeyJoinColumn(name = "id")
public class Doctor extends User{

    @NotNull
    private String experience;

    @NotNull
    @Size(max = 30)
    private String specialization;

    @NotNull
    @Size(max = 2)
    private String rating;

    @ElementCollection
    List<Boolean> schedule;
}
