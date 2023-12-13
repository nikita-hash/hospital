package com.example.hospital.http.controller;

import com.example.hospital.dto.AdminRegistrationDto;
import com.example.hospital.dto.DoctorRegistrationDto;
import com.example.hospital.mapper.AdminMapper;
import com.example.hospital.mapper.DoctortMapper;
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
    @PostMapping("/admin/{id}")
    private String registrationAdmin(@Valid AdminRegistrationDto admin,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable(name = "id")Long idAdmin){
        if(bindingResult.hasErrors()){
                redirectAttributes.addFlashAttribute("adminReg", admin);
            redirectAttributes.addFlashAttribute("bindingResultAdmin", bindingResult);
        }
        else {
            redirectAttributes.addFlashAttribute("successAdminReg", true);
            mailService.sendMessageNewUsers(admin.getEmail(),
                    AdminMapper.INSTANCE.adminRegistrationDtoToAdmin(admin));
            adminService.saveAdmin(admin);

        }
        return "redirect:/admin/"+idAdmin;
    }

    @SneakyThrows
    @PostMapping("/doctor/{id}")
    private String registrationDoctor(@ModelAttribute @Valid DoctorRegistrationDto doctor,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      @PathVariable(name = "id")Long idAdmin) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("doctor", doctor);
            redirectAttributes.addFlashAttribute("bindingResultDoctor", bindingResult);
            return "redirect:/admin/"+idAdmin;
        } else {
            doctorService.saveDoctor(doctor);
            if (mailService.sendMessageNewUsers(doctor.getEmail(), DoctortMapper.INSTANCE.doctorRegistrationDtoToDoctor(doctor)).get()) {
                System.out.println("Прошле этап !");
                redirectAttributes.addFlashAttribute("successDocReg", true);
            } else {
                redirectAttributes.addFlashAttribute("error_internet");
            }
            return "redirect:/admin/"+idAdmin;
        }
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
            patienService.savePatient(patient);
            if(mailService.sendMessageNewUsers(patient.getEmail(),patient).get()){
                redirectAttributes.addFlashAttribute("success",true);
            }
            else {
                redirectAttributes.addFlashAttribute("error_internet");
            }
            return "redirect:/registration";
        }
    }

}
