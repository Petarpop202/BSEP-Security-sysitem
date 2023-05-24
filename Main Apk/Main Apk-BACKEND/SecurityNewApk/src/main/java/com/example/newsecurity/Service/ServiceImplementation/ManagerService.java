package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IManagerRepository;
import com.example.newsecurity.Service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private IManagerRepository managerRepository;

    @Override
    public Manager newManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (!manager.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project");
        }
        return manager.get();
    }

    @Override
    public void deleteManagerById(Long id) {
        managerRepository.deleteById(id);
    }
}
