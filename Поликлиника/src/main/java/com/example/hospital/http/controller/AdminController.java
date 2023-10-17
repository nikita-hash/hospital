package com.example.hospital.http.controller;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import com.example.hospital.model.User;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.ResponseEntity.notFound;

@Controller
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;
    DoctorService doctorService;
    PatienService patienService;
    UserService userService;
    StatisticsService statisticsService;

    @Autowired
    AdminController(AdminService adminService, DoctorService doctorService, PatienService patienService, UserService userRepository,StatisticsService statisticsService){

        this.adminService=adminService;
        this.doctorService=doctorService;
        this.patienService=patienService;
        this.userService =userRepository;
        this.statisticsService=statisticsService;
    }

    @GetMapping("/{id}")
    private String adminMenu(@PathVariable(name = "id")Long id, Model model){

        model.addAttribute("statisticsService",statisticsService);
        model.addAttribute("all_users", userService.findAll().size());
        model.addAttribute("listAdmin",adminService.findAll());
        model.addAttribute("listPatient",patienService.findAll());

        return adminService.findById(id)
                .map(admin -> {
                    admin.getDate_ragistration().getHour();
                    model.addAttribute("admin", admin);
                    model.addAttribute("roles", Role.values());
                    return "admin_menu";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/{id}/avatar")
    public @ResponseBody ResponseEntity<byte[]> findAvatar(@PathVariable("id") Long id) {
        return adminService.findAvatar(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/block:{id_user}")
    public String blockPatient(@PathVariable(name = "id_user")Long id_user,@PathVariable(name = "id")Long id){
        userService.blockUser(id_user);
        return "redirect:/admin/"+id;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/unblock:{id_user}")
    public String unblockPatient(@PathVariable(name = "id_user")Long id_user,@PathVariable(name = "id")Long id){
        userService.unlockUser(id_user);
        return "redirect:/admin/"+id;
    }

    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity<?> deleteUser(@RequestParam(value = "id_patient")Long idUser){
        if(userService.delete(idUser)){
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален !");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден !");
        }
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable(name = "id")Long id,
                         @ModelAttribute @Valid UserChangeDto userChangeDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            FieldError error = new FieldError("userChangeDto", "email", "такая почта уже существует");
            bindingResult.addError(error);
            redirectAttributes.addFlashAttribute("dtoUpdate",userChangeDto);
            redirectAttributes.addFlashAttribute("bindingResultUpdate",bindingResult);
            return "redirect:/admin/{id}";
        }
        else {
            redirectAttributes.addFlashAttribute("successUpdatePers",true);
            return userService.update(id,userChangeDto)
                    .map(it -> "redirect:/admin/{id}")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    }



}
