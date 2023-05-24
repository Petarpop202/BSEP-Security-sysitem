package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Engineer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEngineerService {
    Engineer newEngineer(Engineer engineer);

    List<Engineer> getAllEngineers();

    Engineer getEngineerById(Long id);

    void deleteEngineerById(Long id);
}
