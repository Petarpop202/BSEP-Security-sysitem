package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Address;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.GenderEnum;
import com.example.newsecurity.Repository.IEngineerRepository;
import com.example.newsecurity.Service.IEngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EngineerService implements IEngineerService {
    @Autowired
    private IEngineerRepository engineerRepository;

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
    public void deleteEngineerById(Long id) {
        engineerRepository.deleteById(id);
    }
}
