package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.ServiceImplementation.EngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(value = "/employees",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @PostMapping
    public Employee newEmployee(@RequestBody Employee employee) {
        return employeeService.newEmployee(employee);
    }
    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployeeById(@PathVariable Long id){
        employeeService.deleteEmployeeById(id);
        return "Employee deleted successfully!";
    }
}
