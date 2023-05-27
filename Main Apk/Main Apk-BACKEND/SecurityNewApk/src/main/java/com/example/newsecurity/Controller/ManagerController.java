package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/managers",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class ManagerController {
    @Autowired
    private IManagerService managerService;

    @PostMapping
    public Manager newManager(@RequestBody Manager employee) {
        return managerService.newManager(employee);
    }
    @GetMapping
    public List<Manager> getAllEManagers(){
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public Manager getManagerById(@PathVariable Long id){
        return managerService.getManagerById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteManagerById(@PathVariable Long id){
        managerService.deleteManagerById(id);
        return "Manager deleted successfully!";
    }

    @PutMapping
    public Manager updateManager(@RequestBody ManagerUpdateDTO managerUpdateDTO){
        return managerService.updateManager(managerUpdateDTO);

    }
}
