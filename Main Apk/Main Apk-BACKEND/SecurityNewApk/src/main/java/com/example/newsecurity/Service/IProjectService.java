package com.example.newsecurity.Service;

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
}
