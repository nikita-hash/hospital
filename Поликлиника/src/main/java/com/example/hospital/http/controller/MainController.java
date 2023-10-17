package com.example.hospital.http.controller;

import com.example.hospital.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    MailService mailService;


    @GetMapping("")
    private String main()  {
        return "main";
    }


}
