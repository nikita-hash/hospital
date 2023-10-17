package com.example.hospital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age,LocalDate> {


    @Override
    public void initialize(Age age) {
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate now = LocalDate.now();
        if (dateOfBirth == null) {
            return true; // возраст не требуется при отсутствии даты рождения
        }
        if(dateOfBirth.isAfter(now)){
            return false;
        }
        Period period = Period.between(dateOfBirth, now);
        System.out.println(period.getYears());
        return period.getYears() >= 16 && period.getYears()<=120;
    }
}
