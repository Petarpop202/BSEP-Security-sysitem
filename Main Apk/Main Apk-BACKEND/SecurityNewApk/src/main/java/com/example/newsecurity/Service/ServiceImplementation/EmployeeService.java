package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Repository.IEmployeeRepository;
import com.example.newsecurity.Service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Override
    public Employee newEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(!employee.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return employee.get();
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee updateEmployeeDescription(Long id, String newDesctiption) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Employee not found!"));
        employee.setDescription(newDesctiption);
        return employeeRepository.save(employee);
    }
}
