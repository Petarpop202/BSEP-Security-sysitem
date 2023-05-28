package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Service.IEngineerService;
import com.example.newsecurity.Service.ServiceImplementation.EngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/engineers",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class EngineerController {
    @Autowired
    private IEngineerService engineerService;

    @PostMapping
    public Engineer newEngineer(@RequestBody Engineer engineer) {
        return engineerService.newEngineer(engineer);
    }
    @GetMapping
    public List<Engineer> getAllEngineers(){
        return engineerService.getAllEngineers();
    }

    @GetMapping("/{id}")
    public Engineer getEngineerById(@PathVariable Long id){
        return engineerService.getEngineerById(id);
    }

    @GetMapping("/username={username}")
    public Engineer getEngineerByUsername(@PathVariable String username){
        return engineerService.getEngineerByUsername(username);
    }

    @DeleteMapping("/{id}")
    public String deleteEngineerById(@PathVariable Long id){
        engineerService.deleteEngineerById(id);
        return "Engineer deleted successfully!";
    }
    @PutMapping
    public void updateEngineer(@RequestBody EngineerUpdateDTO engineerUpdateDTO){
        engineerService.updateEngineer(engineerUpdateDTO);
    }
    @PutMapping("/skills-update")
    public void updateEnginnerSkills(@RequestBody EngineerUpdateSkillsDTO engineerUpdateSkillsDTO){
        engineerService.updateEngineerSkills(engineerUpdateSkillsDTO);
    }
}
