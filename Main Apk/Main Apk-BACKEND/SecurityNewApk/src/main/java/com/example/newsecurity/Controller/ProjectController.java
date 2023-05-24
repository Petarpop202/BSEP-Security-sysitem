package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/projects",produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {
    @Autowired
    private IProjectService projectService;

    @PostMapping
    public Project newEngineer(@RequestBody Project project) {
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
}
