package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.ISystemAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/system-administrators",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class SystemAdministratorController {
    @Autowired
    private ISystemAdministratorService systemAdministratorService;
    @Autowired
    private ISystemAdministratorRepository systemAdministratorRepository;

    @GetMapping
    public List<SystemAdministrator> getAllAdministrators(){
        return systemAdministratorService.getAllAdministrators();
    }

    @GetMapping("/{id}")
    public SystemAdministrator getAdministratorById(@PathVariable Long id){
        return systemAdministratorService.getAdministratorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteAdministratorById(@PathVariable Long id){
        systemAdministratorService.deleteAdministratorById(id);
        return "System administrator deleted successfully!";
    }

    @PutMapping
    public SystemAdministrator updateAdministrator(@RequestBody SystemAdministratorUpdateDTO adminDTO){
        return systemAdministratorService.updateAdministrator(adminDTO);

    }
}
