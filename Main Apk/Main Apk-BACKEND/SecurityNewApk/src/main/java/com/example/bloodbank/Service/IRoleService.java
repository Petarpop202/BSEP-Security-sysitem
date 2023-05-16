package com.example.bloodbank.Service;

import com.example.bloodbank.Model.Role;

public interface IRoleService {
    Role findById(Long id);
    Role findByName(String name);
}
