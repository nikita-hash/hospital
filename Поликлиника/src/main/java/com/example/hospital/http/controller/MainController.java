package com.example.hospital.http.controller;

import com.example.hospital.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    MailService mailService;



    @GetMapping("/login")
    private String main()  {
        return "main";
    }


}
