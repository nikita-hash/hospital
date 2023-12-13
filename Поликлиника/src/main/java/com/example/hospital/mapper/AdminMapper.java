package com.example.hospital.mapper;

import com.example.hospital.dto.AdminRegistrationDto;
import com.example.hospital.dto.DoctorRegistrationDto;
import com.example.hospital.model.Admin;
import com.example.hospital.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE= Mappers.getMapper(AdminMapper.class);

    @Mapping(target = "image",ignore = true)
    @Mapping(target = "date_birth", source = "date_birth", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "date_ragistration", source = "date_ragistration",dateFormat = "yyyy-MM-dd HH:mm")
    AdminRegistrationDto adminToAdminRegistrationDto(Admin admin);


    @Mapping(target = "date_birth", source = "date_birth", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "image",ignore = true)
    @Mapping(target = "date_ragistration", source = "date_ragistration",dateFormat = "yyyy-MM-dd HH:mm")
    Admin adminRegistrationDtoToAdmin(AdminRegistrationDto adminRegistrationDto);

}
