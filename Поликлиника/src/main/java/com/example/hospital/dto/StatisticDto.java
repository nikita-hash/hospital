package com.example.hospital.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {


    Integer adminTotal;
    Integer doctorTotal;
    Integer PatientTotal;
    Integer blockTotal;

    List<Map<String,Long>>workLoadWeek;


}
