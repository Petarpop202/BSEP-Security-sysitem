package com.example.newsecurity.Controller;

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
@CrossOrigin
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

    @DeleteMapping("/{id}")
    public String deleteEngineertById(@PathVariable Long id){
        engineerService.deleteEngineerById(id);
        return "Engineer deleted successfully!";
    }
}
