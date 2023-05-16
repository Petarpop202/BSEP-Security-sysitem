package com.example.mainAPK.Service;

import com.example.mainAPK.Model.Role;

public interface IRoleService {
    Role findById(Long id);
    Role findByName(String name);
}
