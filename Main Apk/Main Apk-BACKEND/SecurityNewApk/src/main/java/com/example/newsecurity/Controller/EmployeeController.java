package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.IProjectService;
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
    @Autowired
    private IProjectService projectService;

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

    @GetMapping("/{id}/projects")
    public List<Project> getProjectsByEmployeeId(@PathVariable("id") Long id){
        return projectService.getProjectsByEmployeeId(id);
    }
    @PutMapping("/{id}/description")
    public Employee updateEmployeeDescription(@PathVariable("id") Long id, @RequestBody String newDesctiption){
        return employeeService.updateEmployeeDescription(id, newDesctiption);
    }

}
