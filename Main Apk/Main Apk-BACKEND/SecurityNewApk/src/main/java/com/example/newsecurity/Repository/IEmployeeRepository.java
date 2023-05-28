package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByEngineerId(Long id);
}
