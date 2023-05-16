package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.IUserRepository;
import com.example.newsecurity.Service.IRoleService;
import com.example.newsecurity.Service.IUserService;
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
