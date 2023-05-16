package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.ISystemAdministratorService;

public class SystemAdministratorService implements ISystemAdministratorService {

    private ISystemAdministratorRepository systemAdministratorRepository;

    @Override
    public Iterable<SystemAdministrator> getAll() {
        return null;
    }

    @Override
    public SystemAdministrator getById(Long id) {
        return null;
    }

    @Override
    public SystemAdministrator create(SystemAdministrator entity) {
        return null;
    }

    @Override
    public SystemAdministrator update(SystemAdministrator entity) {
        return null;
    }

    @Override
    public void delete(Long entityId) {

    }

    @Override
    public SystemAdministrator changePassword(Long id, String password) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
}
