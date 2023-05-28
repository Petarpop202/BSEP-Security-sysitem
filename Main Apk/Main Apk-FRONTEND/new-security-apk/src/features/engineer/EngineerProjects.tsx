import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Grid, Button } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { Project } from "../../app/models/Project";
import { Employee } from "../../app/models/Employee";
import { ProjectWithDescription } from "../../app/models/ProjectWithDescription";
import { Link } from "react-router-dom";

export default function EngineerProjects() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [projects, setProjects] = useState<Project[]>([])
    const [employees, setEmployees] = useState<Employee[]>([])
    const [projectsWithDescription, setProjectsWithDescription] = useState<ProjectWithDescription[]>([])
    const [selectedEmployeeId, setSelectedEmployeeId] = useState<number>()
    const [selectedDescription, setSelectedDescription] = useState<string>('')
    let flag = 0;
    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ENGINEER" || user == null){
            navigate('/')
            return
        }
        getProjectsAndDescriptions() 
    }, []);

    useEffect(() => {
        setProjectsWithDescription(projectsWithDescription)
    }, [projectsWithDescription])

    const getProjectsAndDescriptions = () => {
        agent.Engineer.getByUsername(user!.username)
        .then((response_user) =>{
            agent.Employee.getEmployeesByEngineerId(response_user.id)
            .then((response_employees) => {
                    setEmployees(response_employees)
                    for (let i = 0; i < response_employees.length; i++){
                        agent.Employee.getProjectsByEmployeeId(response_employees[i].id)
                        .then((response_projects) =>{
                            let combinedProjects = [...projects, ...response_projects]
                            setProjects(combinedProjects)
                            for (let j = 0; j < response_projects.length; j++){
                                let newProject : ProjectWithDescription = new ProjectWithDescription(
                                    response_projects[j],
                                    response_employees[i].id,
                                    response_employees[i].description
                                )
                                if (!projectsWithDescription.some(element => element.equals(newProject))) {
                                    projectsWithDescription.push(newProject);
                                }
                            }
                        })
                        .catch((error) => console.log(error))
                    }
                })
                .catch((error) => console.log(error))
        })
        .catch((error) => console.log(error))
    }

    const handleEditDescription = (id: number, description: string) => {
        setSelectedEmployeeId(id)
        setSelectedDescription(description)
    }

    const handleChangeDescription = () => {
        agent.Employee.updateEmployeeDescription(selectedEmployeeId, selectedDescription)
            .then((response) => {
                getProjectsAndDescriptions()
            })
            .catch((error) => console.log(error))
    }

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="div" variant="h3" sx={{ mb: 3 }}>
            Projects
        </Typography>
        <Box sx={{ width: '100%' }}>
            <Grid container sx={{mb: 4}}>
                <table style={{width: '100%', textAlign: 'center'}}>
                    <thead>
                        <th>Name</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Description</th>
                        <th>Edit</th>
                    </thead>
                    <tbody>
                        {projectsWithDescription ? projectsWithDescription.map((project) => (
                            <tr key={project.project.id}>
                                <td>{project.project.name}</td>
                                <td>{new Date(project.project.startDate[0], project.project.startDate[1] - 1, project.project.startDate[2], project.project.startDate[3], project.project.startDate[4], 0).toLocaleDateString()}</td>
                                <td>{new Date(project.project.endDate[0], project.project.endDate[1] - 1, project.project.endDate[2], project.project.endDate[3], project.project.endDate[4], 0).toLocaleDateString()}</td>
                                <td>{project.description}</td>
                                <td><Button variant="contained" onClick={() => handleEditDescription(project.employeeId, project.description)}>Edit</Button></td>
                            </tr>
                        )) : <></>}
                    </tbody>
                </table>
            </Grid>
            <Grid container sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography variant="subtitle1" sx={{mb: 1}}>Selected Description</Typography>
                <input type="text" value={selectedDescription} onChange={(e) => setSelectedDescription(e.target.value)}></input>
                <Button variant="contained" color="success" sx={{mt: 2}} onClick={handleChangeDescription}>Save change</Button>
                <Button variant="contained" color="error" component={Link} to="/profile-engineer" sx={{mt: 2}}>Back</Button>
            </Grid>
        </Box>
        </Container>
    );
}
