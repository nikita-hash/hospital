package com.example.hospital.service;

import com.example.hospital.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MailService  {

    @Value("${spring.mail.username}")
    private String mailForm;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostConstruct
    void init(){

    }

    String content;

    @Async
    public CompletableFuture<Boolean> sendMessageNewUsers(String email, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailForm);
        message.setTo(email);
        message.setSubject("Привет");
        content="Добро пожаловать\n";
        content="Вход осуществляется по вашей электронной почте : "+email+"\n";
        content="И сгенерированному системой паролю : "+user.getPassword()+"\n";
        message.setText(content);
        javaMailSender.send(message);
        return CompletableFuture.completedFuture(Boolean.valueOf(true));
    }

}
