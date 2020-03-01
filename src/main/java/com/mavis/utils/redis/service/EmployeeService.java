package com.mavis.utils.redis.service;

import com.mavis.utils.redis.model.Employee;
import com.mavis.utils.redis.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee getEmployee(String id){
        return employeeRepository.findById(id).get();
    }

    public List<Employee> getAllEmployees(){
        List<Employee> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        return employees;
    }

    public void createEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    public void updateEmployeeName(String id, String name){
        Employee employee = getEmployee(id);
        employee.setName(name);
        employeeRepository.save(employee);
    }

    public void deleteEmployee(String id){
        Employee employee = getEmployee(id);
        employeeRepository.deleteById(employee.getId());
    }
}
