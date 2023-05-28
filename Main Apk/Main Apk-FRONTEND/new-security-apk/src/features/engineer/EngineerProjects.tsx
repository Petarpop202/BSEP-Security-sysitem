import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Grid } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { Project } from "../../app/models/Project";

export default function EngineerProjects() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [projects, setProjects] = useState<Project[]>([])

    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ENGINEER" || user == null){
            navigate('/')
            return
        }

        agent.Engineer.getByUsername(user!.username)
            .then((response) =>{
                agent.Employee.getEmployeesByEngineerId(response.id)
                    .then((response) => {
                        agent.Employee.getProjectsByEmployeeId(response[0].id)
                        .then((response) =>{
                            setProjects(response)
                        })
                        .catch((error) => console.log(error))
                    })
                    .catch((error) => console.log(error))
            })
            .catch((error) => console.log(error)) 
    }, []);

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
                    </thead>
                    <tbody>
                        {projects ? projects.map((project) => (
                            <tr key={project.id}>
                                <td>{project.name}</td>
                                <td>{new Date(project.startDate[0], project.startDate[1] - 1, project.startDate[2], project.startDate[3], project.startDate[4], 0).toLocaleDateString()}</td>
                                <td>{new Date(project.endDate[0], project.endDate[1] - 1, project.endDate[2], project.endDate[3], project.endDate[4], 0).toLocaleDateString()}</td>
                            </tr>
                        )) : <></>}
                    </tbody>
                </table>
            </Grid>
        </Box>
        </Container>
    );
}
