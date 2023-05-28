package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Role;
import com.example.newsecurity.Model.Permission;
import com.example.newsecurity.Service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/roles",produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleControlller {
    @Autowired
    private IRoleService roleService;

    @GetMapping
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }

    @GetMapping("/permissions")
    public List<Permission> getAllPermissions(){
        return roleService.getAllPermissions();
    }

    @PostMapping("/{roleName}/permission={permissionName}")
    public void addPermissionToRole(@PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName){
        roleService.addPermissionToRole(roleName, permissionName);
    }

    @DeleteMapping("/{roleName}/permission={permissionName}")
    public void removePermissionFromRole(@PathVariable("roleName") String roleName, @PathVariable("permissionName") String permissionName) {
        roleService.removePermissionFromRole(roleName, permissionName);
    }
}
