package com.example.hospital.service;

import com.example.hospital.dto.DoctorRegistrationDto;
import com.example.hospital.mapper.DoctortMapper;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import com.example.hospital.repository.DoctorRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Service
public class DoctorService {

    @Value("${image.avatar}")
    String avatar;

    ImageServiece imageServiece;

    DoctorRepository doctorRepository;

    PasswordGenerator passwordGenerator;

    @Autowired
    public DoctorService(ImageServiece imageServiece, DoctorRepository doctorRepository, PasswordGenerator passwordGenerator) {
        this.imageServiece = imageServiece;
        this.doctorRepository = doctorRepository;
        this.passwordGenerator = passwordGenerator;
    }

    public List<Doctor>findAll(){
        return doctorRepository.findAll();
    }

    public List<Doctor>findAllByStatusUser(StatusUser statusUser){
        return doctorRepository.findAllByStatusUser(statusUser);
    }


    @SneakyThrows
    @Transactional
    public void  saveDoctor(DoctorRegistrationDto doctorDto){
        Doctor doctor= DoctortMapper.INSTANCE.doctorRegistrationDtoToDoctor(doctorDto);
        doctor.setStatusUser(StatusUser.ACTIVE_STATUS);
        doctor.setRole(Role.DOCTOR);
        doctor.setPassword(passwordGenerator.generateRandomPassword());
        if(!doctorDto.getImage().isEmpty()){
            doctor.setImage(doctorDto.getImage().getOriginalFilename());
            imageServiece.uploadImage(doctorDto.getImage().getOriginalFilename(),doctorDto.getImage().getInputStream());
        }
        else {
            doctor.setImage(avatar);
        }
        doctorRepository.save(doctor);
    }

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }
}
