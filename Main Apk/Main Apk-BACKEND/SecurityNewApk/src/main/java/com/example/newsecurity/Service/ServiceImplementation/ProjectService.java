package com.example.newsecurity.Service.ServiceImplementation;

import com.example.newsecurity.DTO.ProjectReadDto;
import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Repository.IEmployeeRepository;
import com.example.newsecurity.Repository.IProjectRepository;
import com.example.newsecurity.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {
    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IEmployeeRepository employeeRepository;
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

    @Override
    public List<ProjectReadDto> getProjectsByManagerId(Long id) {
        List<Project> projects = projectRepository.findByManagerId(id);
        ProjectReadDto project = new ProjectReadDto();
        List<ProjectReadDto> result = new ArrayList<>();
        for(Project p : projects){
            project.setId(p.getId());
            project.setName(p.getName());
            project.setStartDate(p.getStartDate());
            project.setEndDate(p.getEndDate());
            result.add(project);
        }
        return result;
    }

    @Override
    public void addEmployeeToProject(Long projectId, Long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employee employee = optionalEmployee.get();
            project.addEmployee(employee);
            projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project with the given projectId not found: " + projectId);
        }
    }

    @Override
    public void removeEmployeeFromProject(Long projectId, Long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employee employee = optionalEmployee.get();
            project.removeEmployee(employee);
            projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project with the given projectId not found: " + projectId);
        }
    }

}
