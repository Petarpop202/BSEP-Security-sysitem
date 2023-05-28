package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.EmployeeReadDTO;
import com.example.newsecurity.DTO.EmployeeUpdateDTO;
import com.example.newsecurity.DTO.Jwt;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.*;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.IProjectService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Service.ServiceImplementation.EngineerService;
import com.example.newsecurity.Util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
@RestController
@RequestMapping(value = "/employees",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @PostMapping
    public Employee newEmployee(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Employee employee) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("CREATE_EMPLOYEE")){
            return null;
        }
        return employeeService.newEmployee(employee);
    }
    @GetMapping
    public List<Employee> getAllEmployees(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_EMPLOYEE_ALL")){
            return null;
        }
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_EMPLOYEE_ONE")){
            return null;
        }
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/engineer={id}")
    public List<Employee> getEmployeesByEngineerId(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_EMPLOYEE_BY_ENGINEER")){
            return null;
        }
        return employeeService.getEmployeesByEngineerId(id);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployeeById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_EMPLOYEE")){
            return null;
        }
        employeeService.deleteEmployeeById(id);
        return "Employee deleted successfully!";
    }

    @GetMapping("/{id}/projects")
    public List<Project> getProjectsByEmployeeId(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_PROJECTS_BY_EMPLOYEE")){
            return null;
        }
        return projectService.getProjectsByEmployeeId(id);
    }
    @PutMapping("/{id}/description")
    public Employee updateEmployeeDescription(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id, @RequestBody String newDesctiption){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_EMPLOYEE_DESC")){
            return null;
        }
        return employeeService.updateEmployeeDescription(id, newDesctiption);
    }
    @PutMapping
    public Employee updateEmployee(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EmployeeUpdateDTO employeeUpdateDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_EMPLOYEE")){
            return null;
        }
        return employeeService.updateEmployee(employeeUpdateDTO);
    }

    

    @GetMapping("/project-id/{id}")
    public List<EmployeeReadDTO> getEmployeesByProjectId(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_EMPLOYEES_BY_PROJECT")){
            return null;
        }
        return employeeService.getEmployeesByProjectId(id);
    }
}
