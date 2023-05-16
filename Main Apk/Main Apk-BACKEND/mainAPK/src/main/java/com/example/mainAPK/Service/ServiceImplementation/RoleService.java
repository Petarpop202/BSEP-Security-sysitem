package com.example.mainAPK.Service.ServiceImplementation;

import com.example.mainAPK.Model.Role;
import com.example.mainAPK.Repository.IRoleRepository;
import com.example.mainAPK.Service.IRoleService;
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
