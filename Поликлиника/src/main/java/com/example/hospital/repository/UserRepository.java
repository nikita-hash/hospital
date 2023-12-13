package com.example.hospital.repository;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.model.Role;
import com.example.hospital.model.StatusUser;
import com.example.hospital.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    List<User> findAllByRole(Role role);
    Optional<User> findByEmail(String email);

    @Query("SELECT (COUNT(u) * 100.0 / (SELECT COUNT(u) FROM User u)) FROM User u WHERE u.role = :role")
    Double calculateRolePercentage(@Param("role") Role role);

    @Query("SELECT (COUNT(u) * 100.0 / (SELECT COUNT(u) FROM User u)) FROM User u WHERE u.statusUser = :status")
    Double calculateStatusPercentage(@Param("status") StatusUser status);

}
