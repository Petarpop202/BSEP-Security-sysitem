package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.EmployeeReadDTO;
import com.example.newsecurity.DTO.EmployeeUpdateDTO;
import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEmployeeService {
    Employee newEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    List<Employee> getEmployeesByEngineerId(Long id);

    void deleteEmployeeById(Long id);

    Employee updateEmployeeDescription(Long id, String newDesctiption);

    List<EmployeeReadDTO> getEmployeesByProjectId(Long id);

    Employee updateEmployee(EmployeeUpdateDTO employeeUpdateDTO);
}
