package com.example.hospital.http.controller;

import com.example.hospital.model.*;
import com.example.hospital.service.AdminService;
import com.example.hospital.service.DoctorService;
import com.example.hospital.service.MailService;
import com.example.hospital.service.PatienService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    public RegistrationController(MailService mailService, PatienService patienService, DoctorService doctorService, AdminService adminService) {
        this.mailService = mailService;
        this.patienService = patienService;
        this.doctorService = doctorService;
        this.adminService = adminService;
    }

    private MailService mailService;

    private PatienService patienService;

    private DoctorService doctorService;

    private AdminService adminService;

    @GetMapping("")
    private String registrationMenu(Model model,@ModelAttribute("patient") Patient patient){
        model.addAttribute("patient",patient);
        return "registration";
    }

    @SneakyThrows
    @PostMapping("/admin")
    private String registrationAdmin(@ModelAttribute @Valid Admin admin,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){

        }
        else {

        }
        return "";
    }

    @SneakyThrows
    @PostMapping("/patient")
    private String registrationPatient(@ModelAttribute @Valid Patient patient,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("patient",patient);
            redirectAttributes.addFlashAttribute("bindingResult",bindingResult);
            return "redirect:/registration";
        }
        else {
            if(mailService.sendMessageNewUsers(patient.getEmail(),patient)){
                patienService.savePatient(patient);
                redirectAttributes.addFlashAttribute("success",true);
            }
            else {
                redirectAttributes.addFlashAttribute("error_internet");
            }
            return "redirect:/registration";
        }
    }

}
