package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISystemAdministratorService {
    SystemAdministrator changePassword(Long id, String password);
    User getUserById(Long id);

    List<SystemAdministrator> getAllAdministrators();

    SystemAdministrator getAdministratorById(Long id);

    void deleteAdministratorById(Long id);

    SystemAdministrator updateAdministrator(SystemAdministratorUpdateDTO adminDTO);
}
