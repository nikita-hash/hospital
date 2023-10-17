package com.example.hospital.service;

import com.example.hospital.model.User;

import java.util.List;
import java.util.Optional;

public interface CrudOperation<T,N> {

    List<T> findAll();
    Optional<T>findById(Long id);
    Optional<User> update(Long id, N dto);
    boolean delete(Long id);
}
