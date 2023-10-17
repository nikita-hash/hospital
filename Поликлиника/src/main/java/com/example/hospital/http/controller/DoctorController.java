package com.example.hospital.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class DoctorController {

    @GetMapping("")
    public String main(Model model){
        return"main";
    }
}
