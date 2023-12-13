package com.example.hospital.http.controller;

import com.example.hospital.dto.StatisticDto;
import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.dto.UserSingInDto;
import com.example.hospital.model.*;
import com.example.hospital.model.Record;
import com.example.hospital.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private AdminService adminService;
    private DoctorService doctorService;
    private PatienService patienService;
    private UserService userService;
    private StatisticsService statisticsService;
    private RecordService recordService;
    private MedCardService medCardService;

    @Autowired
    public AdminController(AdminService adminService, DoctorService doctorService,
                           PatienService patienService,
                           UserService userService,
                           StatisticsService statisticsService,
                           RecordService recordService,
                           MedCardService medCardService) {
        this.medCardService = medCardService;
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.patienService = patienService;
        this.userService = userService;
        this.statisticsService = statisticsService;
        this.recordService = recordService;
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    private String adminMenu(@PathVariable(name = "id") Long id, Model model) throws ExecutionException, InterruptedException {
        if (((UserSingInDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId() != id)
            return "redirect:/login";
        StatisticDto statisticDto = statisticsService.calculateStatistics().get();
        User user = userService.findById(id).get();
        List<Record> allRecordWeek = recordService.findAll();
        model.addAttribute("listDoctor", doctorService.findAll());
        model.addAttribute("statistic", statisticDto);
        model.addAttribute("all_users", userService.findAll().size());
        model.addAttribute("listAdmin", adminService.findAll());
        model.addAttribute("listPatient", patienService.findAll());
        model.addAttribute("listRecordToDay", recordService.findRecordsByDateAndUserAndRecordStatus(LocalDate.now(), user, RecordStatus.WAIT));
        model.addAttribute("listAnyClientRecordToday", recordService.findAllToday().stream().filter(record -> record.getUser()
                .getId() != id).collect(Collectors.toList()));
        model.addAttribute("allRecords", allRecordWeek);
        model.addAttribute("numberRecords", allRecordWeek.size());
        model.addAttribute("medicalCard", medCardService.findAllByPatientId(id));
        model.addAttribute("history", recordService.findAllByRecordStatusAndUserId(RecordStatus.COMPLETE, user.getId()));
        return adminService.findById(id)
                .map(admin -> {
                    admin.getDate_ragistration().getHour();
                    model.addAttribute("admin", admin);
                    model.addAttribute("roles", Role.values());
                    return "admin_menu";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/cancel/record")
    public @ResponseBody
    ResponseEntity<?> cancelRecord(@RequestParam(name = "idRecord") Long idRecord) {
        recordService.cancelRecord(idRecord);
        return ResponseEntity.status(HttpStatus.OK).body("Успешно отменено !");
    }

    @GetMapping("/getChart")
    private @ResponseBody
    ResponseEntity<?> getChart() {
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getChart());
    }

    @PostMapping(value = "/record")
    public @ResponseBody
    ResponseEntity<?> createRecord(@RequestBody Record record, @RequestParam(name = "number_day") int nuber,
                                   RedirectAttributes redirectAttributes) {
        if (LocalDate.now().getDayOfWeek().getValue() > nuber) {
            record.setDate(LocalDate.now().plusWeeks(1).minusDays(LocalDate.now().getDayOfWeek().getValue() - nuber));
        } else {
            record.setDate(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - nuber));
        }
        recordService.createRecord(record);
        redirectAttributes.addFlashAttribute("secAddRecord", true);
        return ResponseEntity.status(HttpStatus.OK).body("Запись успешно добавлена !");
    }

    @GetMapping("/open-time")
    public @ResponseBody
    ResponseEntity<?> findOpenTime(@RequestParam(name = "id") Long id, @RequestParam(name = "number_day") int numberDay) {
        List<Record> list = recordService.findOpenTime(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - numberDay),
                doctorService.findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body(list.stream().map(record -> record.getTime()));
    }

    @GetMapping(value = "/{id}/avatar")
    public @ResponseBody
    ResponseEntity<byte[]> findAvatar(@PathVariable("id") Long id) {
        return adminService.findAvatar(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

    @GetMapping("/{id}/block:{id_user}")
    public String blockPatient(@PathVariable(name = "id_user") Long id_user, @PathVariable(name = "id") Long id) {
        userService.blockUser(id_user);
        return "redirect:/admin/" + id;
    }

    @GetMapping("/{id}/unblock:{id_user}")
    public String unblockPatient(@PathVariable(name = "id_user") Long id_user, @PathVariable(name = "id") Long id) {
        userService.unlockUser(id_user);
        return "redirect:/admin/" + id;
    }

    @DeleteMapping("/delete")
    public @ResponseBody
    ResponseEntity<?> deleteUser(@RequestParam(value = "id_patient") Long idUser) {
        if (userService.delete(idUser)) {
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден !");
        }
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable(name = "id") Long id,
                         @ModelAttribute @Valid UserChangeDto userChangeDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            FieldError error = new FieldError("userChangeDto", "email", "такая почта уже существует");
            bindingResult.addError(error);
            redirectAttributes.addFlashAttribute("dtoUpdate", userChangeDto);
            redirectAttributes.addFlashAttribute("bindingResultUpdate", bindingResult);
            return "redirect:/admin/{id}";
        } else {
            redirectAttributes.addFlashAttribute("successUpdatePers", true);
            return userService.update(id, userChangeDto)
                    .map(it -> "redirect:/admin/{id}")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    }


}
