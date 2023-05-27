import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Grid, Button } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { Skills } from "../../app/models/Skills";
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { textAlign, width } from "@mui/system";

export default function EngineerSkills() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [id, setId] = useState<number>();
    const [skills, setSkills] = useState<Skills>()

    const [newName, setNewName] = useState<string>('')
    const [newValue, setNewValue] = useState<number>(0)
    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ENGINEER" || user == null){
            navigate('/')
            return
        }

        agent.Engineer.getByUsername(user!.username)
            .then((response) =>{
                console.log(response)
                setId(response.id)
                setSkills(response.skills)
            })
    }, []);

    const columns: GridColDef[] = [
        {
          field: 'name',
          headerName: 'Name',
        },
        {
          field: 'value',
          headerName: 'Value',
        },
        {
          field: 'delete',
          headerName: 'Delete?',
        },
    ];

    const handleAddSkill = () => {
        if (skills == null){
            setSkills({
                id: id!,
                skills: new Map<string, number>()
            })
        }
        skills?.skills.set(newName, newValue)
        console.log(skills)
    };

    const handleDeleteSkill = (key: string) => {

    }

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="div" variant="h3" sx={{ mb: 3 }}>
            Skills
        </Typography>
        <Box sx={{ width: '100%' }}>
            <Grid>
                <DataGrid
                    getRowId={(row) => row.name}
                    rows={skills ? Array.from(skills!.skills, ([key, value]) => ({ key, value })) : []}
                    columns={columns}
                />
                <table>
                    <thead>
                        <th>Name</th>
                        <th>Value</th>
                        <th>Delete?</th>
                    </thead>
                    <tbody>
                        {skills ? Array.from(skills!.skills.entries()).map(([key, value]) => (
                            <tr key={key}>
                                <td>{key}</td>
                                <td>{value}</td>
                                <td><Button variant="contained" color="error" onClick={() => handleDeleteSkill(key)}></Button></td>
                            </tr>
                        )) : <></>}
                    </tbody>
                </table>
            </Grid>
            <Grid container>
                <Grid item xs={4}>
                    <input type="text" value={newName} onChange={(e) => setNewName(e.target.value)}/>
                </Grid>
                <Grid item xs={4}>
                    <input type="number" value={newValue} onChange={(e) => setNewValue(Number(e.target.value))}/>
                </Grid>
                <Grid item xs={4}>
                    <Button
                    variant="contained"
                    color="success"
                    sx = {{mt: 3, marginRight: '120px'}}
                    onClick={handleAddSkill}
                    >
                        Add Skill
                    </Button> 
                </Grid>
            </Grid>
        </Box>
        </Container>
    );
}
