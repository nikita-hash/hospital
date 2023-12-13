package com.example.hospital.service;


import com.example.hospital.dto.DoctorRecordsListDto;
import com.example.hospital.mapper.RecordMapper;
import com.example.hospital.model.Doctor;
import com.example.hospital.model.Record;
import com.example.hospital.model.RecordStatus;
import com.example.hospital.model.User;
import com.example.hospital.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final DoctorService doctorService;
    private final UserService userService;
    private final RecordRepository recordRepository;

    public void createRecord(Record record){
                record.setDoctor(doctorService.findById(record.getDoctor().getId()).get());
        record.setUser(userService.findById(record.getUser().getId()).get());
        record.setRecordStatus(RecordStatus.WAIT);
        recordRepository.save(record);
    }

    public List<DoctorRecordsListDto>findAllByUserAndRecordStatusOrderByDateDesc(User user,RecordStatus recordStatus){
        List<DoctorRecordsListDto>list= recordRepository.findAllByUserAndRecordStatusOrderByDateDesc(user,recordStatus).stream()
                .map(RecordMapper.INSTANCE::recordToDoctorRecordsListDto)
                .collect(Collectors.toList());
        return list;
    }

    public List<Record>findRecordsByDateAndUserAndRecordStatus(LocalDate date, User user,RecordStatus recordStatus){
        return recordRepository.findRecordsByDateAndUserAndRecordStatus(date ,user,recordStatus);
    }

    public List<Record> findAllByRecordStatus(RecordStatus recordStatus){
        return recordRepository.findAllByRecordStatus(recordStatus);
    }

    public Optional<Record> findById(Long id){
        return recordRepository.findById(id);
    }

    public List<Map<String,Long>> getCountByDayOfWeek(){
        return recordRepository.getCountByDayOfWeek();
    }

    public List<Record>findAllToday(){
        return recordRepository.findAllByDateAndRecordStatus(LocalDate.now(),RecordStatus.WAIT);
    }

    public List<Record>findAll(){
        return recordRepository.findAll();
    }

    public List<Record>findAllByTodayAndUser(LocalDate date, Long id){
        return recordRepository.findAllByDateAndUser(date,
                userService.findById(id).get());
    }

    public List<DoctorRecordsListDto>findAllByDoctorAndRecordStatusOrderByDateDesc(Doctor doctor,RecordStatus recordStatus){
        List<DoctorRecordsListDto>list= recordRepository.findAllByDoctorAndRecordStatusOrderByDateDesc(doctor,recordStatus).stream()
                .map(RecordMapper.INSTANCE::recordToDoctorRecordsListDto)
                .collect(Collectors.toList());
        return list;
    }

    public List<Record>findRecordsByDateAndDoctorAndRecordStatus(LocalDate date,Doctor doctor,RecordStatus recordStatus){
        return recordRepository.findRecordsByDateAndDoctorAndRecordStatus(date,doctor,recordStatus);
    }

    public List<Record>findOpenTime(LocalDate localDate, Doctor doctor){
        return recordRepository.findRecordsByDateAndDoctor(localDate,doctor);
    }

    public List<Record>findAllByRecordStatusAndUserId(RecordStatus recordStatus,Long id){
        return recordRepository.findAllByRecordStatusAndUserId(recordStatus,id);
    }


    public void cancelRecord(Long idRecord) {
        Optional<Record> record=recordRepository.findById(idRecord);
        if(!record.isEmpty()){
            recordRepository.delete(record.get());
        }
    }

    @Transactional
    public void changeStatus(Long recordId, RecordStatus complete) {
            recordRepository.updateRecordStatusById(complete,recordId);
    }
}
