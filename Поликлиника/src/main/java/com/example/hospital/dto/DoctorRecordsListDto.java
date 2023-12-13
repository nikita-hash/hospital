package com.example.hospital.dto;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.User;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.Mapper;

import java.time.DayOfWeek;


@Getter
@Setter
public class DoctorRecordsListDto {

    Long id;
    User user;
    Doctor doctor;
    String dayOfWeek;

}
