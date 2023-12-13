package com.example.hospital.service;

import com.example.hospital.dto.StatisticDto;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class StatisticsService {

    private UserService userService;

    @Autowired
    public StatisticsService(UserService userService, RecordService recordService) {
        this.userService = userService;
        this.recordService = recordService;
    }

    private RecordService recordService;

    public List<Map<String,Long>>getChart(){
        return recordService.getCountByDayOfWeek();
    }


    @Async
    public CompletableFuture<StatisticDto> calculateStatistics() {
        StatisticDto statisticDto=new StatisticDto();
        statisticDto.setAdminTotal((int)userService.calculateRolePercentage(Role.ADMIN));
        statisticDto.setDoctorTotal((int)userService.calculateRolePercentage(Role.DOCTOR));
        statisticDto.setPatientTotal((int)userService.calculateRolePercentage(Role.PATIENT));
        statisticDto.setBlockTotal((int)userService.calculateStatusPercentage(StatusUser.BLOCK_STATUS));
        statisticDto.setWorkLoadWeek(recordService.getCountByDayOfWeek());
        return CompletableFuture.completedFuture(statisticDto);
    }




}
