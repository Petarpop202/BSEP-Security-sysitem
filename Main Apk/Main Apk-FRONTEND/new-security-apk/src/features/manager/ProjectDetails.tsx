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
import { format, parseISO, toDate } from "date-fns";

export default function ProjectDetails () {
    const { id } = useParams();
    const navigate = useNavigate();
    const [project, setProject] = useState<Project | null>(null)
    const [employees, setEmployees] = useState<EmployeeReadDTO[] | null>([])
    const [startDate, setStartDate] = useState<Date | null>(null)
    const [endDate, setEndDate] = useState<Date | null>(null)
    const [description, setDescription] = useState('')
    const [selectedEmployee, setSelectedEmployee] = useState<EmployeeReadDTO | null>(null)
    const [isUpdate, setIsUpdate] = useState(false)
    

    const { user } = useAppSelector((state: { acount: any }) => state.acount)

  useEffect(() => {
        if (user?.userRole !== "ROLE_PROJECT_MANAGER" || user == null){
            navigate('/')
            return
        }
        getProject()
        getEmployees()
    }, [id]);

  const getProject = () => {
    user?.id &&
      agent.Project.getProjectById(id)
        .then((response) => {
          setProject(response)
        })
        .catch((error) => console.log(error))        
  }

  const getEmployees = () => {
    user?.id && 
      agent.Employee.getEmployeesByProjectId(id)
        .then((response) => {
          setEmployees(response)
          console.log(response)
        })
        .catch((error) => console.log(error))
  }

  const handleStartDate = (value : any) => {
    setStartDate(value);
  }
  const handleEndDate = (value : any) => {
    setEndDate(value);
  }
  const handleDesription = (value: any) => {
    setDescription(value);
  }

  const formatDate = (dateArray: number[]) => {
    const [year, month, day] = dateArray;
    const formattedDate = `${day < 10 ? "0" + day : day}.${month < 10 ? "0" + month : month}.${year}.`;
    return formattedDate;
  };

  

  const updateEmployee = () =>{

    if(startDate === null || endDate === null || description === null){
        toast.error('Fields can not be empty!')
        return
    }

    let updateEmployeeDto = {
        id: selectedEmployee?.id,
        startDate: startDate,
        endDate: endDate
    }
    agent.Employee.updateEmployee(updateEmployeeDto)
        .then(() => console.log('uppp'))
        .catch(error => console.log(error))

    agent.Employee.updateEmployeeDescription(selectedEmployee?.id, description)
        .then(() => toast.success('updated'))
        .catch(error => console.log(error))
  }

    return (
        <>
        <Typography variant="h2" sx={{justifyContent: 'center', alignItems: 'center', mt: 5, mb: 5, textAlign: 'center'}}>{project?.name}</Typography>
        <Typography variant="h4" sx={{justifyContent: 'center', alignItems: 'center', mb: 3}}>Employees:</Typography>
        
        {isUpdate &&
        <>
            <Typography variant="h4" sx={{justifyContent: 'center', alignItems: 'center', mt: 5, mb: 1, textAlign: 'left'}}>{selectedEmployee?.name} {selectedEmployee?.surname}</Typography>
        <TableContainer component={Paper} sx={{mb: 5}}>
        <Table>
          <TableBody>
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
                <TableCell>Description</TableCell>
                <TableCell>
                    <TextField
                    variant="outlined"
                    value={description}
                    onChange={(e) => handleDesription(e.target.value)}
                  />
                </TableCell>                    
            </TableRow>   
            <TableRow>      
                <TableCell>
                </TableCell>
                <TableCell>
                    <Button onClick={updateEmployee} variant="contained" sx={{ml: 5}}>Update</Button>
                    <Button onClick={() => setIsUpdate(false)} variant="contained" sx={{ml: 5}}>Cancel</Button>
                </TableCell>                   
            </TableRow> 
          </TableBody>
        </Table>
      </TableContainer>
        </>
        }
        
        
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell align="left">Surname</TableCell>
                  <TableCell align="left">Description</TableCell>
                  <TableCell align="left">Start</TableCell>
                  <TableCell align="left">End</TableCell>
                  <TableCell align="left"></TableCell>
                </TableRow>
              </TableHead>
              {employees && <TableBody>
                {employees.map((employee) => (
                  <TableRow
                    key={employee.id}
                  >
                    <TableCell component="th" scope="row">
                      {employee.name}
                    </TableCell>
                    <TableCell>
                      {employee.surname}
                    </TableCell>
                    <TableCell>
                      {employee.description}
                    </TableCell>
                    <TableCell>
                      {new Date(employee.startDate[0], employee.startDate[1] - 1, employee.startDate[2] + 1, employee.startDate[3], employee.startDate[4], 0).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      {new Date(employee.endDate[0], employee.endDate[1] - 1, employee.endDate[2] + 1, employee.endDate[3], employee.endDate[4], 0).toLocaleDateString()}
                    </TableCell>
                    <TableCell>
                      <Button variant="contained" onClick={() => {handleDesription(employee.description); setSelectedEmployee(employee); setIsUpdate(true); console.log(employee.startDate.toString())}}>Update</Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>}
              
            </Table>
          </TableContainer>
        </>
        
    )
}