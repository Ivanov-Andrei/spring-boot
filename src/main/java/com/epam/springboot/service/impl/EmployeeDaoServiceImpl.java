package com.epam.springboot.service.impl;

import com.epam.springboot.model.Employee;
import com.epam.springboot.repository.EmployeeRepository;
import com.epam.springboot.service.EmployeeDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeDaoServiceImpl implements EmployeeDaoService {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    @Transactional
    public void save(Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findByNameAndSurname(employee.getName(), employee.getSurname());
        if (existingEmployee.isPresent()) {
            Employee entity = existingEmployee.get();
            entity.setName(employee.getName());
            entity.setSurname(employee.getSurname());
            entity.setPosition(employee.getPosition());
            employeeRepository.save(entity);
        } else {
            employeeRepository.save(employee);
        }
    }

    @Override
    public Optional<Employee> findByNameAndSurname(String name, String surname) {
        return employeeRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    @Override
    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    @Override
    @Transactional
    public void delete(Employee employee) {
        Optional<Employee> existingEmployee = findByNameAndSurname(employee.getName(), employee.getSurname());
        if (existingEmployee.isPresent()) {
            employeeRepository.delete(existingEmployee.get());
        } else {
            log.info(String.format("there is no employee with name %s and surname %s", employee.getName(), employee.getSurname()));
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }
}
