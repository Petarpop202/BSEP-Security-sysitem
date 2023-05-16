package com.example.newsecurity.Service;

import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;

public interface ISystemAdministratorService extends ICRUDService<SystemAdministrator> {
    SystemAdministrator changePassword(Long id, String password);
    User getUserById(Long id);
}
