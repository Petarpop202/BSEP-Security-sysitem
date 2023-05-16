package com.example.bloodbank.Service.ServiceImplementation;

import com.example.bloodbank.Model.User;
import com.example.bloodbank.Repository.IUserRepository;
import com.example.bloodbank.Service.IRoleService;
import com.example.bloodbank.Service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private IUserRepository _userRepository;
    private IRoleService _roleService;

    @Override
    public Iterable<User> getAll() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public User getByVerificationCode(String code) {
        return null;
    }

    @Override
    public User activate(User u) {
        return null;
    }
}
