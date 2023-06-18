package com.example.newsecurity.Controller;

import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.Alarm;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.IAlarmService;
import com.example.newsecurity.Service.ISystemAdministratorService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/system-administrators",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class SystemAdministratorController {
    @Autowired
    private ISystemAdministratorService systemAdministratorService;
    @Autowired
    private ISystemAdministratorRepository systemAdministratorRepository;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAlarmService alarmService;

    private static final Logger logger = LogManager.getLogger(SystemAdministratorController.class);
    @GetMapping
    public List<SystemAdministrator> getAllAdministrators(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ADMIN_ALL")){
            return null;
        }
        return systemAdministratorService.getAllAdministrators();
    }

    @GetMapping("/{id}")
    public SystemAdministrator getAdministratorById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id) throws Exception {
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
        logger.warn("User {} deleted administrator with id: {}.", username, id);
        return "System administrator deleted successfully!";
    }

    @PutMapping
    public SystemAdministrator updateAdministrator(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SystemAdministratorUpdateDTO adminDTO) throws Exception {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ADMIN")){
            return null;
        }
        logger.info("User {} changed administrator {} profile.", username,adminDTO.getUsername());
        return systemAdministratorService.updateAdministrator(adminDTO);
    }

    @PutMapping("/{id}")
    public SystemAdministrator updatePassword(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id, @RequestBody String newPassword){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("UPDATE_ADMIN_PASSWORD")){
            return null;
        }
        logger.info("User {} updated password.", username);
        return systemAdministratorService.updatePassword(id, newPassword);
    }

    @GetMapping("/logs")
    public List<String> getLogs() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("logs.log"));
        return lines;
    }

    @GetMapping("/alarms")
    public List<Alarm> getAlarms() throws IOException {
        List<Alarm> alarms = (List<Alarm>) alarmService.getAll();
        return alarms;
    }

//    @GetMapping("/update-alarms")
//    public Boolean updateAlarms() throws IOException {
//        List<Alarm> alarms = (List<Alarm>) alarmService.update();
//        return true ;
//    }

    @GetMapping("/isAlarmed")
    public boolean isAlarmed() throws IOException {
        boolean is = alarmService.IsAlarmed();
        if(!is)
            alarmService.update();
        return is;
    }

    @GetMapping("/setAlarm")
    public boolean setAlarm() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String ipAddress = "192.168.1.1";
        alarmService.checkIp(ipAddress);
        return true;
    }
    @PutMapping("/blockUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public User blockUser(@PathVariable String username){
        return userService.blockUser(username);
    }
    @PutMapping("/unblockUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public User unblockUser(@PathVariable String username){
        return userService.unblockUser(username);
    }
}
