package com.example.newsecurity.Service;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProjectService {
    Project newProject(Project project);

    List<Project> getAllProjects();

    Project getProjectById(Long id);

    void deleteProjectById(Long id);

    List<Project> getProjectsByEmployeeId(Long id);

    List<Project> getProjectsByManagerId(Long id);

    void addEmployeeToProject(Long projectId, Long employeeId);

    void removeEmployeeFromProject(Long projectId, Long employeeId);
}
