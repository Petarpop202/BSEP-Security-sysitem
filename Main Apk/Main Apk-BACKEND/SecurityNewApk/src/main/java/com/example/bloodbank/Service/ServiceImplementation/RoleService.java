package com.example.bloodbank.Service.ServiceImplementation;

import com.example.bloodbank.Model.Role;
import com.example.bloodbank.Repository.IRoleRepository;
import com.example.bloodbank.Service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    private IRoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public Role findByName(String name) {
        return null;
    }
}
