package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IProjectRepository;
import com.example.newsecurity.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {
    @Autowired
    private IProjectRepository projectRepository;
    @Override
    public Project newProject(Project project) {
        return projectRepository.save(project);
    }
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find project");
        }
        return project.get();
    }

    @Override
    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<Project> getProjectsByEmployeeId(Long id) {
        return projectRepository.findByEmployeesId(id);
    }

}
