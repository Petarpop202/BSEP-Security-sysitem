import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Grid, Button } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";
import { Link } from "react-router-dom";

export default function EngineerSkills() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [id, setId] = useState<number>();
    const [skills, setSkills] = useState<Map<string, number>>(new Map<string, number>())
    const [tableData, setTableData] = useState<Array<[string, number]>>([])

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
                setId(response.id)
                setTableData(Array.from(new Map<string, number>(Object.entries(response.skills)).entries()))
                setSkills(new Map<string, number>(Object.entries(response.skills)))
            })
            .catch((error) => console.log(error)) 
    }, []);

    const handleAddSkill = () => {
        if (skills === null || skills?.size === undefined){
            setSkills(new Map<string, number>())
        }else {
            updateSkills()
        }
    };

    useEffect(() =>{
        if (newName === '' || newValue < 1 || newValue > 5){
            return
        }
        updateSkills()
    }, [skills]);

    const handleDeleteSkill = (key: string) => {
        skills!.delete(key)
        let newSkills = {
            id: id,
            skills: Object.fromEntries(skills!)
        }
        agent.Engineer.updateEngineerSkills(newSkills)
            .then((reponse) => {
                const transformedData = Array.from(skills!.entries())
                setTableData(transformedData)
            })
            .catch((error) => console.log(error))
    }

    const updateSkills = () => {
        if (newValue < 1 || newValue > 5)
        {
            toast.error('Value needs to be between 1 and 5!')
            return
        }

        if (newName === ''){
            toast.error('Name cannot be empty!')
            return
        }
        skills!.set(newName, newValue)
        let newSkills = {
            id: id,
            skills: Object.fromEntries(skills!)
        }
        setNewName('')
        setNewValue(0)
        agent.Engineer.updateEngineerSkills(newSkills)
            .then((response) => {
                const transformedData = Array.from(skills!.entries())
                setTableData(transformedData)
            })
            .catch((error) => console.log(error))
    }

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="div" variant="h3" sx={{ mb: 3 }}>
            Skills
        </Typography>
        <Box sx={{ width: '100%' }}>
            <Grid container sx={{mb: 4}}>
                <table style={{width: '100%', textAlign: 'center'}}>
                    <thead>
                        <th>Name</th>
                        <th>Value</th>
                        <th>Action</th>
                    </thead>
                    <tbody>
                        {skills ? tableData.map(([key, value]) => (
                            <tr key={key}>
                                <td>{key}</td>
                                <td>{value}</td>
                                <td><Button variant="contained" color="error" onClick={() => handleDeleteSkill(key)}>Delete</Button></td>
                            </tr>
                        )) : <></>}
                    </tbody>
                </table>
            </Grid>
            <hr/>
            <Grid container sx={{ p: 4, display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                <Grid item xs={6} height={50}>
                    <Typography variant="subtitle1">
                        Name
                    </Typography>
                    <input type="text" value={newName} onChange={(e) => setNewName(e.target.value)}/>
                </Grid>
                <Grid item xs={6}>
                    <Typography variant="subtitle1">
                        Value
                    </Typography>
                    <input type="number" value={newValue} min={1} max={5} onChange={(e) => setNewValue(Number(e.target.value))}/>
                </Grid>
            </Grid>
            <Grid container sx={{paddingX: 2}}>
                <Button
                sx={{mb: 4}}
                variant="contained"
                color="success"
                onClick={handleAddSkill}
                fullWidth
                >
                    Add Skill
                </Button>
            </Grid>
                <hr/>
            <Grid container sx={{paddingX: 2}}>
                <Button
                sx={{mt: 4}}
                variant="contained"
                color="error"
                component={Link}
                to="/profile-engineer"
                fullWidth
                >
                    Back
                </Button> 
            </Grid>
        </Box>
        </Container>
    );
}
