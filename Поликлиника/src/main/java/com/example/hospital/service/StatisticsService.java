package com.example.hospital.service;

import com.example.hospital.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private UserService userService;

    @Autowired
    StatisticsService(UserService userService){
        this.userService=userService;
    }

    public int getTotalAdmin(){
        return userService.findAllByRole(Role.ADMIN).size();
    }

    public int getTotalDoctor(){
        return userService.findAllByRole(Role.DOCTOR).size();
    }

    public int getTotalPatient(){
        return userService.findAllByRole(Role.PATIENT).size();
    }

}
