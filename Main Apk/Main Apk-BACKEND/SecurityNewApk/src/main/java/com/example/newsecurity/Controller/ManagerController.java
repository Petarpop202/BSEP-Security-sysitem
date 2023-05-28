package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.ManagerReadDTO;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IEmployeeService;
import com.example.newsecurity.Service.IManagerService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/managers",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class ManagerController {
    @Autowired
    private IManagerService managerService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @PostMapping
    public Manager newManager(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Manager employee) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("CREATE_MANAGER")){
            return null;
        }
        return managerService.newManager(employee);
    }
    @GetMapping
    public List<Manager> getAllEManagers(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_MANAGER_ALL")){
            return null;
        }
        return managerService.getAllManagers();
    }

    @GetMapping("/{id}")
    public ManagerReadDTO getManagerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_MANAGER_ONE")){
            return null;
        }
        return managerService.getManagerById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteManagerById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_MANAGER")){
            return null;
        }
        managerService.deleteManagerById(id);
        return "Manager deleted successfully!";
    }

    @PutMapping
    public Manager updateManager(@RequestHeader("Authorization") String authorizationHeader, @RequestBody ManagerUpdateDTO managerUpdateDTO){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_MANAGER")){
            return null;
        }
        return managerService.updateManager(managerUpdateDTO);
    }

    @PutMapping("/{id}")
    public Manager updatePassword(@PathVariable Long id, @RequestBody String newPassword){
        return managerService.updatePassword(id, newPassword);
    }
}
