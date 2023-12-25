package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.ManagerReadDTO;
import com.example.newsecurity.DTO.ManagerUpdateDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Manager;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IManagerRepository;
import com.example.newsecurity.Service.IEncryptService;
import com.example.newsecurity.Service.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private IManagerRepository managerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IEncryptService encryptService;

    @Override
    public Manager newManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public List<Manager> getAllManagers() throws Exception {
        List<Manager> managers = managerRepository.findAll();
        List<Manager> managersDecrypted = new ArrayList<>();
        if (managers == null){
            return null;
        }
        for (Manager manager: managers){
            managersDecrypted.add(readManager(manager));
        }
        return managersDecrypted;
    }

    @Override
    public ManagerReadDTO getManagerById(Long id) throws Exception {
        Optional<Manager> managerOptional = managerRepository.findById(id);
        if (!managerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project");
        }
        Manager manager = managerOptional.get();
        ManagerReadDTO managerReadDTO = new ManagerReadDTO();

        if(manager.getSurname().equals("Managerovic")){
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

        managerReadDTO.setId(manager.getId());
        managerReadDTO.setName(manager.getName());
        managerReadDTO.setSurname(manager.getSurname());
        managerReadDTO.setMail(encryptService.decryptFile(manager.getMail(), manager.getUsername(), "mail"));
        managerReadDTO.setUsername(manager.getUsername());
        managerReadDTO.setPhoneNumber(encryptService.decryptFile(manager.getPhoneNumber(), manager.getUsername(), "phoneNumber"));
        managerReadDTO.setJmbg(encryptService.decryptFile(manager.getJmbg(), manager.getUsername(), "jmbg"));
        managerReadDTO.setAddress(manager.getAddress());

        return managerReadDTO;


    }

    @Override
    public void deleteManagerById(Long id) {
        managerRepository.deleteById(id);
    }

    @Override
    public Manager updateManager(ManagerUpdateDTO managerUpdateDTO) throws Exception {
        Manager manager = managerRepository.findById(managerUpdateDTO.getId()).orElseThrow(() -> new NoSuchElementException("Manager not found!"));
        manager.setName(managerUpdateDTO.getName());
        manager.setSurname(managerUpdateDTO.getSurname());
        manager.setMail(encryptService.encryptFile(managerUpdateDTO.getMail(), managerUpdateDTO.getUsername(), "mail"));
        manager.setUsername(managerUpdateDTO.getUsername());
        manager.setPhoneNumber(encryptService.encryptFile(managerUpdateDTO.getPhoneNumber(), managerUpdateDTO.getUsername(), "phoneNumber"));
        manager.setJmbg(encryptService.encryptFile(managerUpdateDTO.getJmbg(), managerUpdateDTO.getUsername(), "jmbg"));
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

    public Manager readManager(Manager managerToRead) throws Exception {
        Manager manager = managerToRead;
        if(manager.getSurname().equals("Managerovic")){
            return manager;
        }
        manager.setPhoneNumber(encryptService.decryptFile(managerToRead.getPhoneNumber(), managerToRead.getUsername(), "phoneNumber"));
        manager.setJmbg(encryptService.decryptFile(managerToRead.getJmbg(), managerToRead.getUsername(), "jmbg"));
        manager.setMail(encryptService.decryptFile(managerToRead.getMail(), managerToRead.getUsername(), "mail"));
        return manager;
    }
}
