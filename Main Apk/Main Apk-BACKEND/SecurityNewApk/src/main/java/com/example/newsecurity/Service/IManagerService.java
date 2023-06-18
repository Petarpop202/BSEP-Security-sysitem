package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.ManagerReadDTO;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IManagerService {
    Manager newManager(Manager manager);

    List<Manager> getAllManagers() throws Exception;

    ManagerReadDTO getManagerById(Long id) throws Exception;

    void deleteManagerById(Long id);

    Manager updateManager(ManagerUpdateDTO managerUpdateDTO) throws Exception;

    Manager updatePassword(Long id, String newPassword);
}
