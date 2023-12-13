package com.example.hospital.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
    @Size(max = 5,min = 1)
    @Builder.Default
    private Integer rating=5;

    @ElementCollection
    List<Boolean> schedule;
}
