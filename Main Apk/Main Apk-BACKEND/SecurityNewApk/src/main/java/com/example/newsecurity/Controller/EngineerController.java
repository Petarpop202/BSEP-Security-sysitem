package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IEngineerService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
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
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @PostMapping
    public Engineer newEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Engineer engineer) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("CREATE_ENGINEER")){
            return null;
        }
        return engineerService.newEngineer(engineer);
    }
    @GetMapping
    public List<Engineer> getAllEngineers(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ENGINEER_ALL")){
            return null;
        }
        return engineerService.getAllEngineers();
    }

    @GetMapping("/{id}")
    public Engineer getEngineerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ENGINEER_ONE")){
            return null;
        }
        return engineerService.getEngineerById(id);
    }

    @GetMapping("/username={username}")
    public Engineer getEngineerByUsername(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String username){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String token_username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(token_username);
        if (!user.hasPermission("GET_ENGINEER_BY_USERNAME")){
            return null;
        }
        return engineerService.getEngineerByUsername(username);
    }

    @DeleteMapping("/{id}")
    public String deleteEngineerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_ENGINEER")){
            return null;
        }
        engineerService.deleteEngineerById(id);
        return "Engineer deleted successfully!";
    }
    @PutMapping
    public void updateEngineer(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateDTO engineerUpdateDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER")){
            return;
        }
        engineerService.updateEngineer(engineerUpdateDTO);
    }
    @PutMapping("/skills-update")
    public void updateEnginnerSkills(@RequestHeader("Authorization") String authorizationHeader, @RequestBody EngineerUpdateSkillsDTO engineerUpdateSkillsDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ENGINEER_SKILLS")){
            return;
        }
        engineerService.updateEngineerSkills(engineerUpdateSkillsDTO);
    }

    @PutMapping("/{id}")
    public Engineer updatePassword(@PathVariable Long id, @RequestBody String newPassword){
        return engineerService.updatePassword(id, newPassword);
    }
}
