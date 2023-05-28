package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.EmployeeReadDTO;
import com.example.newsecurity.DTO.EmployeeUpdateDTO;
import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IEmployeeRepository;
import com.example.newsecurity.Repository.IProjectRepository;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.IEngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private IEmployeeRepository employeeRepository;
    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IEngineerService engineerService;
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
    public List<Employee> getEmployeesByEngineerId(Long id) {
        return employeeRepository.findAllByEngineerId(id);
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

    @Override
    public List<EmployeeReadDTO> getEmployeesByProjectId(Long id) {
        Optional<Project> pr = projectRepository.findById(id);
        if(!pr.isPresent()){
            throw new NoSuchElementException("Project not found!");
        }
        Project project = pr.get();

        List<Employee> employees = project.getEmployees();

        List<EmployeeReadDTO> result = new ArrayList<>();
        EmployeeReadDTO employeeReadDTO = new EmployeeReadDTO();

        for(Employee emp : employees){
            employeeReadDTO.setId(emp.getId());
            employeeReadDTO.setName(emp.getEngineer().getName());
            employeeReadDTO.setSurname(emp.getEngineer().getSurname());
            employeeReadDTO.setDescription(emp.getDescription());
            employeeReadDTO.setEndDate(emp.getEndDate());
            employeeReadDTO.setStartDate(emp.getStartDate());

            if(!result.contains(employeeReadDTO)){
                result.add(employeeReadDTO);
            }
        }

        return result;
    }

    @Override
    public Employee updateEmployee(EmployeeUpdateDTO employeeUpdateDTO) {
        Employee employee = employeeRepository.findById(employeeUpdateDTO.getId()).orElseThrow(() -> new NoSuchElementException("Employee not found!"));
        employee.setStartDate(employeeUpdateDTO.getStartDate());
        employee.setEndDate(employeeUpdateDTO.getEndDate());
        return employeeRepository.save(employee);
    }
}
