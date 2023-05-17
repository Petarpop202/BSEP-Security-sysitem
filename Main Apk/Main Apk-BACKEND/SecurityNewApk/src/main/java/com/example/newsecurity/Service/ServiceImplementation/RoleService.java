package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Role;
import com.example.newsecurity.Repository.IRoleRepository;
import com.example.newsecurity.Service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
