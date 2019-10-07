package com.aivanov.springboot.service;

import com.aivanov.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDaoService {

    void save(Employee employee);

    Optional<Employee> findByNameAndSurname(String name, String surname);

    List<Employee> findAll();

    void deleteAll();

    void saveAll(List<Employee> employees);

    void delete(Employee employee);

    Optional<Employee> findById(Long id);
}
