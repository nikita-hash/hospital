package com.example.hospital.service;

import com.example.hospital.model.*;
import com.example.hospital.repository.AdminRepository;
import com.example.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Value("${image.avatar}")
    String avatar;

    AdminRepository adminRepository;

    ImageServiece imageServiece;

    PasswordGenerator passwordGenerator;

    UserRepository userRepository;


    @Autowired
    AdminService(AdminRepository adminRepository, PasswordGenerator passwordGenerator, ImageServiece imageServiece, UserRepository userRepository){
        this.adminRepository=adminRepository;
        this.imageServiece=imageServiece;
        this.passwordGenerator=passwordGenerator;
        this.userRepository=userRepository;
    }

    public List<Admin>findAll(){
        return adminRepository.findAll();
    }

    @Transactional
    public void saveAdmin(Admin admin){
        admin.setStatusUser(StatusUser.ACTIVE_STATUS);
        admin.setRole(Role.ADMIN);
        admin.setImage(avatar);
        admin.setDate_ragistration(LocalDateTime.now());
        admin.setPassword(passwordGenerator.generateRandomPassword());
        adminRepository.save(admin);
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageServiece::getImage);
    }


    public Optional<Admin> findById(Long id){
        return adminRepository.findById(id);
    }



}
