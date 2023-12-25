package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Permission;
import com.example.newsecurity.Model.Role;
import com.example.newsecurity.Repository.IPermissionRepository;
import com.example.newsecurity.Repository.IRoleRepository;
import com.example.newsecurity.Service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public void addPermissionToRole(String roleName, String permissionName) {
        Optional<Permission> optionalPermission = Optional.ofNullable(permissionRepository.findByName(permissionName));
        Optional<Role> optionalRole = Optional.ofNullable(roleRepository.findByName(roleName));
        if (optionalPermission.isPresent() && optionalRole.isPresent()) {
            Permission permission = optionalPermission.get();
            Role role = optionalRole.get();
            role.addPermission(permission);
            roleRepository.save(role);
        } else {
            throw new IllegalArgumentException("Permission with the given name not found: " + permissionName);
        }
    }

    @Override
    public void removePermissionFromRole(String roleName, String permissionName) {
        Optional<Permission> optionalPermission = Optional.ofNullable(permissionRepository.findByName(permissionName));
        Optional<Role> optionalRole = Optional.ofNullable(roleRepository.findByName(roleName));
        if (optionalPermission.isPresent() && optionalRole.isPresent()) {
            Permission permission = optionalPermission.get();
            Role role = optionalRole.get();
            role.removePermission(permission);
            roleRepository.save(role);
        } else {
            throw new IllegalArgumentException("Permission with the given name not found: " + permissionName);
        }
    }
}
