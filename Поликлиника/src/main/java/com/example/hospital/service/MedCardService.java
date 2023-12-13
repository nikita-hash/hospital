package com.example.hospital.service;

import com.example.hospital.model.MedicalCard;
import com.example.hospital.model.RecordStatus;
import com.example.hospital.repository.MedCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedCardService {

    MedCardRepository medCardRepository;
    RecordService recordService;

    @Autowired
    public MedCardService(MedCardRepository medCardRepository,RecordService recordService) {
        this.medCardRepository = medCardRepository;
        this.recordService=recordService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(MedicalCard medicalCard,Long recordId){
        medCardRepository.save(medicalCard);
        recordService.changeStatus(recordId, RecordStatus.COMPLETE);
    }

    public Optional<MedicalCard>findById(Long id){
        return medCardRepository.findById(id);
    }


    public List<MedicalCard> findAllByPatientId(Long id) {
        return medCardRepository.findAllByPatientId(id);
    }
}
