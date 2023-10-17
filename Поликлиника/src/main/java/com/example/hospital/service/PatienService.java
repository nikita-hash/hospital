package com.example.hospital.service;

import com.example.hospital.model.Patient;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import com.example.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatienService {

    @Value("${image.avatar}")
    String avatar;

    PasswordGenerator passwordGenerator;

    PatientRepository patientRepository;

    @Autowired
    public PatienService(PatientRepository patientRepository, PasswordGenerator passwordGenerator) {
        this.patientRepository = patientRepository;
        this.passwordGenerator = passwordGenerator;
    }

    //Тестовый вариант !
    @Transactional
    public void updateStatus(Long id,StatusUser newStatus){
        Patient patient= patientRepository.findById(id).get();
        patient.setStatusUser(newStatus);
        patientRepository.save(patient);
    }

    public List<Patient> findAll(){
        return patientRepository.findAll();
    }

    public Optional<Patient>findById(Long id){
        return patientRepository.findById(id);
    }

    @Transactional
    public void  savePatient(Patient patient){
        patient.setStatusUser(StatusUser.ACTIVE_STATUS);
        patient.setRole(Role.PATIENT);
        patient.setImage(avatar);
        patient.setPassword(passwordGenerator.generateRandomPassword());
        patientRepository.save(patient);
    }


}
