package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Permission;
import com.example.newsecurity.Model.Role;

import java.util.List;

public interface IRoleService {
    List<Role> getAllRoles();

    List<Permission> getAllPermissions();

    Role findById(Long id);
    Role findByName(String name);

    void addPermissionToRole(String role, String permission);

    void removePermissionFromRole(String role, String permission);
}
