package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.ManagerReadDTO;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IManagerRepository;
import com.example.newsecurity.Service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private IManagerRepository managerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Manager newManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public ManagerReadDTO getManagerById(Long id) {
        Optional<Manager> managerOptional = managerRepository.findById(id);
        if (!managerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project");
        }
        Manager manager = managerOptional.get();
        ManagerReadDTO managerReadDTO = new ManagerReadDTO();

        managerReadDTO.setId(manager.getId());
        managerReadDTO.setName(manager.getName());
        managerReadDTO.setSurname(manager.getSurname());
        managerReadDTO.setMail(manager.getMail());
        managerReadDTO.setUsername(manager.getUsername());
        managerReadDTO.setPhoneNumber(manager.getPhoneNumber());
        managerReadDTO.setJmbg(manager.getJmbg());
        managerReadDTO.setAddress(manager.getAddress());

        return managerReadDTO;


    }

    @Override
    public void deleteManagerById(Long id) {
        managerRepository.deleteById(id);
    }

    @Override
    public Manager updateManager(ManagerUpdateDTO managerUpdateDTO) {
        Manager manager = managerRepository.findById(managerUpdateDTO.getId()).orElseThrow(() -> new NoSuchElementException("Manager not found!"));
        manager.setName(managerUpdateDTO.getName());
        manager.setSurname(managerUpdateDTO.getSurname());
        manager.setMail(managerUpdateDTO.getMail());
        manager.setUsername(managerUpdateDTO.getUsername());
        manager.setPhoneNumber(managerUpdateDTO.getPhoneNumber());
        manager.setJmbg(managerUpdateDTO.getJmbg());
        manager.setAddress(managerUpdateDTO.getAddress());

        return managerRepository.save(manager);
    }

    @Override
    public Manager updatePassword(Long id, String newPassword) {
        Optional<Manager> optionalManager = managerRepository.findById(id);
        if(!optionalManager.isPresent()){
            throw new NoSuchElementException("Manager not found!");
        }
        Manager manager = optionalManager.get();
        manager.setPassword(passwordEncoder.encode(newPassword));

        return managerRepository.save(manager);
    }
}
