package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.StatusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

    List<Doctor> findAllByStatusUser(StatusUser statusUser);
}
