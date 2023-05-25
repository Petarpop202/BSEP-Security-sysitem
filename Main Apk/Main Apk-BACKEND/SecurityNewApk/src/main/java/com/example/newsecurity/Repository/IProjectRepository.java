package com.example.newsecurity.Repository;

import com.example.newsecurity.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByEmployeesId(Long id);
}
