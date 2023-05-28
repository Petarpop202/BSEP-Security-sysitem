package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.ManagerReadDTO;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IManagerService {
    Manager newManager(Manager manager);

    List<Manager> getAllManagers();

    ManagerReadDTO getManagerById(Long id);

    void deleteManagerById(Long id);

    Manager updateManager(ManagerUpdateDTO managerUpdateDTO);

    Manager updatePassword(Long id, String newPassword);
}
