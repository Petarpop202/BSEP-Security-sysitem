package com.example.mainAPK.Service.ServiceImplementation;

import com.example.mainAPK.Model.SystemAdministrator;
import com.example.mainAPK.Model.User;
import com.example.mainAPK.Repository.ISystemAdministratorRepository;
import com.example.mainAPK.Service.ISystemAdministratorService;

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
