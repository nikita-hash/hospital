package com.example.hospital.http.controller;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.dto.UserSingInDto;
import com.example.hospital.model.*;
import com.example.hospital.model.Record;
import com.example.hospital.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    DoctorService doctorService;
    RecordService recordService;
    AdminService adminService;
    PdfDocumentService pdfDocumentService;
    MedCardService medCardService;
    UserService userService;

    @Autowired
    public DoctorController(DoctorService doctorService,RecordService recordService,AdminService adminService,
                            PdfDocumentService pdfDocumentService,
                            MedCardService medCardService,
                            UserService userService) {
        this.doctorService = doctorService;
        this.recordService=recordService;
        this.adminService=adminService;
        this.pdfDocumentService=pdfDocumentService;
        this.medCardService=medCardService;
        this.userService=userService;
    }

    @GetMapping("/{id}")
    public String main(Model model, @PathVariable(name = "id")Long id)
    {
        if (((UserSingInDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId() != id) return "redirect:/login";
        Doctor doctor=doctorService.findById(id).get();
        model.addAttribute("recordWeek",recordService.findAllByDoctorAndRecordStatusOrderByDateDesc(doctor, RecordStatus.WAIT));
        model.addAttribute("recordToday",recordService.findRecordsByDateAndDoctorAndRecordStatus(LocalDate.now(),doctor,RecordStatus.WAIT));
        model.addAttribute("listDoctor",doctorService.findAllByStatusUser(StatusUser.ACTIVE_STATUS).stream()
                .filter(doctor1 -> doctor1.getId()!=doctor.getId()).collect(Collectors.toList()));
        model.addAttribute("medicalCard",medCardService.findAllByPatientId(id));
        model.addAttribute("history",recordService.findAllByRecordStatusAndUserId(RecordStatus.COMPLETE,doctor.getId()));
        model.addAttribute("doctor",doctor);
        return"doctor_menu";
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
            return "redirect:/doctor/{id}";
        }
        else {

            redirectAttributes.addFlashAttribute("successUpdatePers",true);
            return userService.update(id,userChangeDto)
                    .map(it -> "redirect:/doctor/{id}")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
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

    @SneakyThrows
    @GetMapping("/getFile/{id}")
    public void getFile(@PathVariable(name = "id")Long id, HttpServletResponse response){
        MedicalCard medicalCard=medCardService.findById(id).get();
        ByteArrayOutputStream baos=pdfDocumentService.writeBlankRecord(medicalCard);
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"pdf_запись_" + medicalCard.getDate_visitation() + ".pdf\"";
        response.setHeader(headerKey, headerValue);
        response.setContentLength(baos.size());
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }

    @GetMapping("/cancel/record")
    public @ResponseBody ResponseEntity<?> cancelRecord(@RequestParam(name = "idRecord")Long idRecord){
        recordService.cancelRecord(idRecord);
        return ResponseEntity.status(HttpStatus.OK).body("Успешно отменено !");
    }

    @PostMapping(value = "/record")
    public @ResponseBody ResponseEntity<?> createRecord(@RequestBody Record record,@RequestParam(name = "number_day")int nuber,
                                                        RedirectAttributes redirectAttributes){
        if(LocalDate.now().getDayOfWeek().getValue()>nuber){
            record.setDate(LocalDate.now().plusWeeks(1).minusDays(LocalDate.now().getDayOfWeek().getValue() - nuber));
        }
        else {
            record.setDate(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - nuber));
        }
        recordService.createRecord(record);
        redirectAttributes.addFlashAttribute("secAddRecord",true);
        return ResponseEntity.status(HttpStatus.OK).body("Запись успешно добавлена !");
    }

    @GetMapping("/open-time")
    public @ResponseBody ResponseEntity<?>findOpenTime(@RequestParam(name = "id")Long id,@RequestParam(name = "number_day")int numberDay){
        List<Record> list=recordService.findOpenTime(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - numberDay),
                doctorService.findById(id).get());
        return ResponseEntity.status(HttpStatus.OK).body(list.stream().map(record->record.getTime()));
    }



}
