package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/projects",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "https://localhost:3000")
public class ProjectController {
    @Autowired
    private IProjectService projectService;

    @PostMapping
    public Project newProject(@RequestBody Project project) {
        return projectService.newProject(project);
    }
    @GetMapping
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProjectById(@PathVariable Long id){
        projectService.deleteProjectById(id);
        return "Project deleted successfully!";
    }

    @GetMapping("/manager/{id}")
    public List<Project> getProjectsByManagerId(@PathVariable("id") Long id){
        return projectService.getProjectsByManagerId(id);
    }

    @PostMapping("/{projectId}/employee={employeeId}")
    public void addEmployeeToProject(@PathVariable("projectId") Long projectId, @PathVariable("employeeId") Long employeeId){
        projectService.addEmployeeToProject(projectId, employeeId);
    }

    @DeleteMapping("/{projectId}/employee={employeeId}")
    public void removeEmployeeFromProject(@PathVariable("projectId") Long projectId, @PathVariable("employeeId") Long employeeId) {
        projectService.removeEmployeeFromProject(projectId, employeeId);
    }
}
