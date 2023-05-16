package com.example.mainAPK.Service;

import com.example.mainAPK.Model.SystemAdministrator;
import com.example.mainAPK.Model.User;

public interface ISystemAdministratorService extends ICRUDService<SystemAdministrator> {
    SystemAdministrator changePassword(Long id, String password);
    User getUserById(Long id);
}
