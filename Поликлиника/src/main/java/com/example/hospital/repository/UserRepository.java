package com.example.hospital.repository;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.model.Role;
import com.example.hospital.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    List<User> findAllByRole(Role role);
    Optional<User> findByEmail(String email);


}
