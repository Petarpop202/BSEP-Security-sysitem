package com.example.newsecurity.Service;

import com.example.newsecurity.DTO.EngineerUpdateDTO;
import com.example.newsecurity.DTO.EngineerUpdateSkillsDTO;
import com.example.newsecurity.Model.Engineer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.PrivateKey;
import java.util.List;

@Service
public interface IEngineerService {
    Engineer newEngineer(Engineer engineer) throws Exception;

    List<Engineer> getAllEngineers() throws Exception;

    Engineer getEngineerById(Long id) throws Exception;

    Engineer getEngineerByUsername(String username) throws Exception;

    void deleteEngineerById(Long id);
    void updateEngineer(EngineerUpdateDTO engineerUpdateDTO) throws Exception;

    void updateEngineerSkills(EngineerUpdateSkillsDTO engineerUpdateSkillsDTO) throws Exception;
    Engineer updatePassword(Long id, String newPassword);
    String uploadCV(Long id, MultipartFile file) throws Exception;
}
