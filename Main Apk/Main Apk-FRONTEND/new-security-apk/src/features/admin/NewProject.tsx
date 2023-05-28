import { useEffect, useState } from "react"
import { Project } from "../../app/models/Project"
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate, useParams } from "react-router-dom";
import agent from "../../app/api/agent";
import { EmployeeReadDTO } from "../../app/models/EmployeeReadDTO";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button, Typography, TextField} from '@mui/material';
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { toast } from "react-toastify";


export default function NewProject() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const navigate = useNavigate();
    const [project, setProject] = useState<Project | null>(null)
    const [startDate, setStartDate] = useState<Date | null>(null)
    const [endDate, setEndDate] = useState<Date | null>(null)
    const [name, setName] = useState<String>('')
    const [isUpdate, setIsUpdate] = useState(false)


    if (user?.userRole !== "ROLE_ADMINISTRATOR" || user == null){
            navigate('/')            
        }

    const handleStartDate = (value : any) => {
    setStartDate(value);
    }
    const handleEndDate = (value : any) => {
        setEndDate(value);
    }

    const handleName = (value: any) => {
        setName(value)
    }

    const createProject = () => {

        if(name === '' || startDate === null || endDate === null){
            toast.error('Fields can not be empty')
            return
        }

        let newProject = {
            name: name,
            startDate: startDate,
            endDate: endDate,
            employees: null,
            manager: null
        }

        agent.Project.createProject(newProject)
            .then(() => {
                toast.success('created')
                navigate('/admin-projects')
            })
            .catch(error => console.log(error))
    }

    return (
        <>            
                <TableContainer component={Paper} sx={{mb: 5}}>
                <Table>
                <TableBody>
                    <TableRow>      
                        <TableCell>Project name</TableCell>
                        <TableCell>
                            <TextField
                            variant="outlined"
                            value={name}
                            onChange={(e) => handleName(e.target.value)}
                        />
                        </TableCell>                    
                    </TableRow> 
                    <TableRow>
                    <TableCell>Start date</TableCell>               
                        <TableCell>                   
                            <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DateTimePicker
                                label="Start date"
                                value={startDate}
                                onChange={handleStartDate}
                                slotProps={{ textField: {  required: true } }}
                            />
                        </LocalizationProvider>            
                        </TableCell>
                    </TableRow> 
                    <TableRow>      
                    <TableCell>End date</TableCell>               
                        <TableCell>                   
                            <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DateTimePicker
                                label="End date"
                                value={endDate}
                                onChange={handleEndDate}
                                slotProps={{ textField: {  required: true } }}
                            />
                        </LocalizationProvider>            
                        </TableCell>
                    </TableRow>     
                      
                    <TableRow>      
                        <TableCell>
                        </TableCell>
                        <TableCell>
                            <Button onClick={createProject} variant="contained" sx={{ml: 5}}>Create</Button>
                            <Button onClick={() => navigate('/admin-projects')} variant="contained" sx={{ml: 5}}>Cancel</Button>
                        </TableCell>                   
                    </TableRow> 
                </TableBody>
                </Table>
            </TableContainer>
        </>
        
    )
}