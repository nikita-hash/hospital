package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Getter
@Setter
@Entity
@Table(name = "medical_card")
public class MedicalCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 5000)
    @NotNull
    @Size(max = 5000, message = "Слишком длинное описание !")
    @NotBlank(message = "Поле не должно быть пустым !")
    String diagnosis;

    String date_visitation;

    @Column(length = 5000)
    @NotNull
    @Size(max = 5000, message = "Слишком длинное описание !")
    @NotBlank(message = "Поле не должно быть пустым !")
    String description;

    @Column(length = 5000)
    @NotNull
    @Size(max = 5000, message = "Слишком длинное описание !")
    @NotBlank(message = "Поле не должно быть пустым !")
    String appointments;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    User patient;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id",nullable = true)
    Doctor doctor;

}
