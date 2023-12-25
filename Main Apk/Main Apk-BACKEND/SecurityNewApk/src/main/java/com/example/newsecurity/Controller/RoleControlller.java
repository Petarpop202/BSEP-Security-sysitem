package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Role;
import com.example.newsecurity.Model.Permission;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IRoleService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/roles",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class RoleControlller {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    private static final Logger logger = LogManager.getLogger(RoleControlller.class);
    @GetMapping
    public List<Role> getAllRoles(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_ROLE_ALL")){
            return null;
        }
        return roleService.getAllRoles();
    }

    @GetMapping("/permissions")
    public List<Permission> getAllPermissions(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_PERMISSION_ALL")){
            return null;
        }
        return roleService.getAllPermissions();
    }

    @PostMapping("/{roleName}/permission={permissionName}")
    public void addPermissionToRole(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("ADD_PERMISSION_TO_ROLE")){
            return;
        }
        logger.info("User {} get new permission {} to role {}.", username,permissionName,roleName);
        roleService.addPermissionToRole(roleName, permissionName);
    }

    @DeleteMapping("/{roleName}/permission={permissionName}")
    public void removePermissionFromRole(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("REMOVE_PERMISSION_FROM_ROLE")){
            return;
        }
        logger.warn("User {} removed permission {} from role {}.", username,permissionName,roleName);
        roleService.removePermissionFromRole(roleName, permissionName);
    }
}
