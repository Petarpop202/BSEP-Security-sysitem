package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IManagerService {
    Manager newManager(Manager manager);

    List<Manager> getAllManagers();

    Manager getManagerById(Long id);

    void deleteManagerById(Long id);

    Manager updateManager(ManagerUpdateDTO managerUpdateDTO);
}
