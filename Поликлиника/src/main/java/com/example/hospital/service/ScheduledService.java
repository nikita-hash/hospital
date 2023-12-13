package com.example.hospital.service;

import com.example.hospital.model.Record;
import com.example.hospital.model.RecordStatus;
import com.example.hospital.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ScheduledService {

    @Autowired
    private RecordService recordService;


    @Scheduled(cron = "0 */5 * * * *")
    public void reportCurrentTime() {
        List<Record> list=recordService.findAllByRecordStatus(RecordStatus.WAIT);
        list.forEach(record -> {
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime specifiedTime = LocalTime.parse(record.getTime(), formatter);

            if (currentTime.isAfter(specifiedTime)) {
               recordService.changeStatus(record.getId(), RecordStatus.COMPLETE);
            }
        });
    }
}
