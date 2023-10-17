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
@Table(name = "patient_table")
@PrimaryKeyJoinColumn(name = "id")
public class Patient extends User{

    @NotNull
    private String sex;

    @OneToMany(mappedBy = "patient")
    List<MedicalCard> medicalCardList;

    /*@OneToOne(cascade = CascadeType.ALL,mappedBy = "patient")
    @Valid
    private MedicalCard medicalCard;*/

}
