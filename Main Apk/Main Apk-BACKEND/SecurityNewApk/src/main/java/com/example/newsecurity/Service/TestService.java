package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Test;
import com.example.newsecurity.Repository.ITestRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("firstService")
public class TestService {
    private ITestRepository _repo;

    TestService(ITestRepository repo){
        _repo = repo;
    }
    public List<Test> getAll(){
        List<Test> all = _repo.findAll();
        return all;
    }
}
