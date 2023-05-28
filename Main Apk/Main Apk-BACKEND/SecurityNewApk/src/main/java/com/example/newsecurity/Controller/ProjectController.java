package com.example.newsecurity.Controller;

import com.example.newsecurity.Model.Employee;
import com.example.newsecurity.Model.Engineer;
import com.example.newsecurity.Model.Project;
import com.example.newsecurity.Model.User;
import com.example.newsecurity.Service.IProjectService;
import com.example.newsecurity.Service.IUserService;
import com.example.newsecurity.Util.TokenUtils;
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
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;

    @PostMapping
    public Project newProject(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Project project) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("CREATE_PROJECT")){
            return null;
        }
        return projectService.newProject(project);
    }
    @GetMapping
    public List<Project> getAllProjects(@RequestHeader("Authorization") String authorizationHeader){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_PROJECT_ALL")){
            return null;
        }
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_PROJECT_ONE")){
            return null;
        }
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProjectById(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("DELETE_PROJECT")){
            return null;
        }
        projectService.deleteProjectById(id);
        return "Project deleted successfully!";
    }

    @GetMapping("/manager/{id}")
    public List<Project> getProjectsByManagerId(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("GET_PROJECTS_BY_MANAGER")){
            return null;
        }
        return projectService.getProjectsByManagerId(id);
    }

    @PostMapping("/{projectId}/employee={employeeId}")
    public void addEmployeeToProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("projectId") Long projectId, @PathVariable("employeeId") Long employeeId){
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("ADD_EMPLOYEE_TO_PROJECT")){
            return;
        }
        projectService.addEmployeeToProject(projectId, employeeId);
    }

    @DeleteMapping("/{projectId}/employee={employeeId}")
    public void removeEmployeeFromProject(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("projectId") Long projectId, @PathVariable("employeeId") Long employeeId) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        String username = tokenUtils.getUsernameFromToken(jwtToken);
        User user = userService.findByUsername(username);
        if (!user.hasPermission("REMOVE_EMPLOYEE_FROM_PROJECT")){
            return;
        }
        projectService.removeEmployeeFromProject(projectId, employeeId);
    }
}
