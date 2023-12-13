package com.example.hospital.mapper;

import com.example.hospital.dto.DoctorRegistrationDto;
import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DoctortMapper {

    DoctortMapper INSTANCE= Mappers.getMapper(DoctortMapper.class);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "image",ignore = true)
    @Mapping(target = "date_birth", source = "date_birth", dateFormat = "yyyy-MM-dd")
    DoctorRegistrationDto doctorToDoctorRegistrationDto(Doctor doctor);


    @Mapping(target = "date_birth", source = "date_birth", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "id",source = "id")
    @Mapping(target = "image",ignore = true)
    Doctor doctorRegistrationDtoToDoctor(DoctorRegistrationDto doctorRegistrationDto);
}
