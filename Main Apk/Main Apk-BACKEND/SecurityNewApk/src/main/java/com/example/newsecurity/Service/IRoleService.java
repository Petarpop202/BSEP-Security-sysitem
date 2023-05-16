package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Role;

public interface IRoleService {
    Role findById(Long id);
    Role findByName(String name);
}
