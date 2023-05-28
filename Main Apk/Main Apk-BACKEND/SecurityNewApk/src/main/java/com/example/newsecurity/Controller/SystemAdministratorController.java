package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.ISystemAdministratorService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/system-administrators",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class SystemAdministratorController {
    @Autowired
    private ISystemAdministratorService systemAdministratorService;
    @Autowired
    private ISystemAdministratorRepository systemAdministratorRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @GetMapping
    public List<SystemAdministrator> getAllAdministrators(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ADMIN_ALL")){
            return null;
        }
        return systemAdministratorService.getAllAdministrators();
    }

    @GetMapping("/{id}")
    public SystemAdministrator getAdministratorById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ADMIN_ONE")){
            return null;
        }
        return systemAdministratorService.getAdministratorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteAdministratorById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_ADMIN")){
            return null;
        }
        systemAdministratorService.deleteAdministratorById(id);
        return "System administrator deleted successfully!";
    }

    @PutMapping
    public SystemAdministrator updateAdministrator(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SystemAdministratorUpdateDTO adminDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ADMIN")){
            return null;
        }
        return systemAdministratorService.updateAdministrator(adminDTO);
    }
}
