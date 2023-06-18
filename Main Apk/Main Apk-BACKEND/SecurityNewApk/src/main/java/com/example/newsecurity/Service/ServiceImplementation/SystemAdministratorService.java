package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.SystemAdministratorUpdateDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.SystemAdministrator;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Repository.ISystemAdministratorRepository;
import com.example.newsecurity.Service.IEncryptService;
import com.example.newsecurity.Service.ISystemAdministratorService;
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
public class SystemAdministratorService implements ISystemAdministratorService {
    @Autowired
    private ISystemAdministratorRepository systemAdministratorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IEncryptService encryptService;
    @Override
    public SystemAdministrator changePassword(Long id, String password) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public List<SystemAdministrator> getAllAdministrators() throws Exception {

        List<SystemAdministrator> admins = systemAdministratorRepository.findAll();
        List<SystemAdministrator> adminsDecrypted = new ArrayList<>();
        if (admins == null){
            return null;
        }
        for (SystemAdministrator admin : admins){
            adminsDecrypted.add(readSystemAdministrator(admin));
        }
        return adminsDecrypted;
    }

    @Override
    public SystemAdministrator getAdministratorById(Long id) throws Exception {
        Optional<SystemAdministrator> administrator = systemAdministratorRepository.findById(id);
        if (!administrator.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find administrator");
        }
        return readSystemAdministrator(administrator.get());
    }

    @Override
    public void deleteAdministratorById(Long id) {
        systemAdministratorRepository.deleteById(id);
    }

    @Override
    public SystemAdministrator updateAdministrator(SystemAdministratorUpdateDTO adminDTO) throws Exception {
        SystemAdministrator administrator = systemAdministratorRepository.findById(adminDTO.getId()).orElseThrow(() -> new NoSuchElementException("Manager not found!"));
        administrator.setName(adminDTO.getName());
        administrator.setSurname(adminDTO.getSurname());
        administrator.setMail(encryptService.encryptFile(adminDTO.getMail(), adminDTO.getUsername(), "mail"));
        administrator.setUsername(adminDTO.getUsername());
        administrator.setPhoneNumber(encryptService.encryptFile(adminDTO.getPhoneNumber(), adminDTO.getUsername(), "phoneNumber"));
        administrator.setJmbg(encryptService.encryptFile(adminDTO.getJmbg(), adminDTO.getUsername(), "jmbg"));
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

    public SystemAdministrator readSystemAdministrator(SystemAdministrator admintoRead) throws Exception {
        SystemAdministrator admin = admintoRead;
        if(admin.getSurname().equals("Adminovic")){
            return admin;
        }
        admin.setPhoneNumber(encryptService.decryptFile(admintoRead.getPhoneNumber(), admintoRead.getUsername(), "phoneNumber"));
        admin.setJmbg(encryptService.decryptFile(admintoRead.getJmbg(), admintoRead.getUsername(), "jmbg"));
        admin.setMail(encryptService.decryptFile(admintoRead.getMail(), admintoRead.getUsername(), "mail"));
        return admin;
    }
}
