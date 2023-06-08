package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Repository.IEngineerRepository;
import com.example.newsecurity.Service.IEngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EngineerService implements IEngineerService {
    private static final String path = "src/main/resources/cv";
    @Autowired
    private IEngineerRepository engineerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Engineer newEngineer(Engineer engineer) {
        return engineerRepository.save(engineer);
    }

    @Override
    public List<Engineer> getAllEngineers() {
        return engineerRepository.findAll();
    }

    @Override
    public Engineer getEngineerById(Long id) {
        Optional<Engineer> engineer = engineerRepository.findById(id);
        if(!engineer.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return engineer.get();
    }

    @Override
    public Engineer getEngineerByUsername(String username){
        Optional<Engineer> engineer = Optional.ofNullable(engineerRepository.findByUsername(username));
        if (!engineer.isPresent()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
        }
        return engineer.get();
    }

    @Override
    public void deleteEngineerById(Long id) {
        engineerRepository.deleteById(id);
    }

    @Override
    public void updateEngineer(EngineerUpdateDTO engineerUpdateDTO) {
        Engineer engineer = getEngineerById(engineerUpdateDTO.getId());
        if(engineer != null){
            engineer.setName(engineerUpdateDTO.getName());
            engineer.setSurname(engineerUpdateDTO.getSurname());
            engineer.setUsername(engineerUpdateDTO.getUsername());
            engineer.setPhoneNumber(engineerUpdateDTO.getPhoneNumber());
            engineer.setJmbg(engineerUpdateDTO.getJmbg());
            engineer.setGender(engineerUpdateDTO.getGender());
            engineer.setAddress(engineerUpdateDTO.getAddress());
            engineerRepository.save(engineer);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
    }

    @Override
    public void updateEngineerSkills(EngineerUpdateSkillsDTO engineerUpdateSkillsDTO) {
        Engineer engineer = getEngineerById(engineerUpdateSkillsDTO.getId());
        if(engineer != null){
            engineer.setSkills(engineerUpdateSkillsDTO.getSkills());
            engineerRepository.save(engineer);
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find engineer");
    }

    public String uploadCV(Long id, MultipartFile file) throws Exception {
        Engineer engineer = getEngineerById(id);
        if (!file.getOriginalFilename().isEmpty()){
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path, engineer.getUsername() + "_CV.pdf")));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            throw new Exception();
        }
        File files = new File(path);
        return Arrays.stream(files.list()).filter(cv -> cv.contains(engineer.getUsername())).toList().get(0);
    }

    @Override
    public Engineer updatePassword(Long id, String newPassword) {
        Optional<Engineer> optionalEngineer = engineerRepository.findById(id);
        if(!optionalEngineer.isPresent()){
            throw new NoSuchElementException("Engineer not found!");
        }
        Engineer engineer = optionalEngineer.get();
        engineer.setPassword(passwordEncoder.encode(newPassword));

        return engineerRepository.save(engineer);
    }
}
