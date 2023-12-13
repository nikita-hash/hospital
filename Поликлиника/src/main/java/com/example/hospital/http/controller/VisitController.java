package com.example.hospital.http.controller;

import com.example.hospital.model.MedicalCard;
import com.example.hospital.model.Record;
import com.example.hospital.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;

@Controller
@RequestMapping("/visit")
public class VisitController {

    UserService userService;
    PatienService patienService;
    AdminService adminService;
    MedCardService medCardRepository;
    PdfDocumentService pdfDocumentService;
    RecordService recordService;
    DoctorService doctorService;

    @Autowired
    public VisitController(UserService userService, PatienService patienService,AdminService adminService,
                           PdfDocumentService pdfDocumentService,RecordService recordService,
                           MedCardService medCardRepository,
                           DoctorService doctorService) {
        this.userService = userService;
        this.patienService = patienService;
        this.adminService=adminService;
        this.medCardRepository=medCardRepository;
        this.pdfDocumentService=pdfDocumentService;
        this.recordService=recordService;
        this.doctorService=doctorService;
    }


    @GetMapping("/record{id}")
    public String visitPatient(@PathVariable(name = "id")Long id,Model model){
        Optional<Record> record =recordService.findById(id);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date=dateFormatter.format(LocalDate.now());
        model.addAttribute("patient",record.get().getUser());
        model.addAttribute("doctor",record.get().getDoctor());
        model.addAttribute("idRecord",id);
        model.addAttribute("date",date);
        return "visit";
    }

    @GetMapping("/getFile")
    private void getFile(HttpServletResponse response, jakarta.servlet.http.HttpSession httpSession) throws IOException {
        MedicalCard medicalCard=(MedicalCard)httpSession.getAttribute("medCard");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        ByteArrayOutputStream baos=pdfDocumentService.writeBlankRecord(medicalCard);
        String currentDateTime = dateFormatter.format(new Date());
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"pdf_" + currentDateTime + ".pdf\"";
        response.setHeader(headerKey, headerValue);
        response.setContentLength(baos.size());
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }

    @SneakyThrows
    @PostMapping("/record{id}/complite")
    private String complite(@PathVariable(name = "id")Long id,
                            RedirectAttributes  model,
                            @Valid MedicalCard medicalCard,
                            BindingResult bindingResult,
                            HttpServletResponse response,
                            jakarta.servlet.http.HttpSession modelMed){
        if(!bindingResult.hasErrors()){
            Optional<Record>record=recordService.findById(id);
            medicalCard.setDoctor(doctorService.findById(record
                    .get()
                    .getDoctor()
                    .getId())
                    .get());
            medicalCard.setPatient(
                    userService.findById(record
                                    .get()
                                    .getUser()
                                    .getId())
                                    .get()
            );
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateTime = dateFormatter.format(new Date());
            medicalCard.setDate_visitation(currentDateTime);
            model.addFlashAttribute("cardReg", medicalCard);
            model.addFlashAttribute("success", true);
            modelMed.setAttribute("medCard",medicalCard);
            medCardRepository.save(medicalCard,id);
            return "redirect:/visit/record"+id;
        }
        else{
            model.addFlashAttribute("cardReg", medicalCard);
            model.addFlashAttribute("bindingResult", bindingResult);
            return "redirect:/visit/record"+id;
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

    @GetMapping("/logout/{id}")
    public String exit(@PathVariable(name = "id")Long id){
        return "redirect:/doctor/"+id;
    }

    @PostMapping("/complite")
    private ResponseEntity<?> complite(@Valid MedicalCard medicalCard,
                                       BindingResult bindingResult){

        return ResponseEntity.status(HttpStatus.OK).body("good");
    }



}
