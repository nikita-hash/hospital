package com.example.hospital.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@Component
public class ScheduledConfig {
    private static SimpleDateFormat simpl = new SimpleDateFormat("dd-MM-yyyy HH:mm");


    @Scheduled(fixedRate = 2000)
    public void reportCurrentTime() {

    }
}
