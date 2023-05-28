package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.ISystemAdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class SystemAdministratorService implements ISystemAdministratorService {
    @Autowired
    private ISystemAdministratorRepository systemAdministratorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public SystemAdministrator changePassword(Long id, String password) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public List<SystemAdministrator> getAllAdministrators() {
        return systemAdministratorRepository.findAll();
    }

    @Override
    public SystemAdministrator getAdministratorById(Long id) {
        Optional<SystemAdministrator> administrator = systemAdministratorRepository.findById(id);
        if (!administrator.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find administrator");
        }
        return administrator.get();
    }

    @Override
    public void deleteAdministratorById(Long id) {
        systemAdministratorRepository.deleteById(id);
    }

    @Override
    public SystemAdministrator updateAdministrator(SystemAdministratorUpdateDTO adminDTO) {
        SystemAdministrator administrator = systemAdministratorRepository.findById(adminDTO.getId()).orElseThrow(() -> new NoSuchElementException("Manager not found!"));
        administrator.setName(adminDTO.getName());
        administrator.setSurname(adminDTO.getSurname());
        administrator.setMail(adminDTO.getMail());
        administrator.setUsername(adminDTO.getUsername());
        administrator.setPhoneNumber(adminDTO.getPhoneNumber());
        administrator.setJmbg(adminDTO.getJmbg());
        administrator.setGender(adminDTO.getGender());
        administrator.setAddress(adminDTO.getAddress());

        return systemAdministratorRepository.save(administrator);
    }

    @Override
    public SystemAdministrator updatePassword(Long id, String newPassword) {
        Optional<SystemAdministrator> optionalAdmin = systemAdministratorRepository.findById(id);
        if(!optionalAdmin.isPresent()){
            throw new NoSuchElementException("System Administrator not found!");
        }
        SystemAdministrator admin = optionalAdmin.get();
        admin.setPassword(passwordEncoder.encode(newPassword));

        return systemAdministratorRepository.save(admin);
    }
}
