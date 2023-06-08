package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IEngineerService {
    Engineer newEngineer(Engineer engineer);

    List<Engineer> getAllEngineers();

    Engineer getEngineerById(Long id);

    Engineer getEngineerByUsername(String username);

    void deleteEngineerById(Long id);
    void updateEngineer(EngineerUpdateDTO engineerUpdateDTO);

    void updateEngineerSkills(EngineerUpdateSkillsDTO engineerUpdateSkillsDTO);
    String uploadCV(Long id, MultipartFile file) throws Exception;
    Engineer updatePassword(Long id, String newPassword);
}
