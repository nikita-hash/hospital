package com.example.hospital.repository;

import com.example.hospital.model.MedicalCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedCardRepository extends JpaRepository<MedicalCard, Long> {

    List<MedicalCard>findAllByPatientId(Long id);
}
