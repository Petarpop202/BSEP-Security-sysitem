import { useEffect, useState } from "react"
import { Project } from "../../app/models/Project"
import { store, useAppSelector } from "../../app/apk/configureApk";
import { Link, useNavigate, useParams } from "react-router-dom";
import agent from "../../app/api/agent";
import { EmployeeReadDTO } from "../../app/models/EmployeeReadDTO";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button, Typography, TextField, Select, Grid} from '@mui/material';
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { toast } from "react-toastify";
import { format, parseISO, toDate } from "date-fns";
import { Manager } from "../../app/models/Manager";
import { refreshUser } from "../account/accountSlice";
import { Document, Page, pdfjs } from "react-pdf/dist/esm";
import 'react-pdf/dist/esm/Page/AnnotationLayer.css';
import 'react-pdf/dist/esm/Page/TextLayer.css';

export default function ProjectDetails () {
    pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

    const { id } = useParams();
    const navigate = useNavigate();
    const [project, setProject] = useState<Project | null>(null)
    const [employees, setEmployees] = useState<EmployeeReadDTO[] | null>([])
    const [startDate, setStartDate] = useState<Date | null>(null)
    const [endDate, setEndDate] = useState<Date | null>(null)
    const [description, setDescription] = useState('')
    const [selectedEmployee, setSelectedEmployee] = useState<EmployeeReadDTO | null>(null)
    const [isUpdate, setIsUpdate] = useState(false)
    const [engineers, setEngineers] = useState<Manager[] | null>([])
    const [engineerId, setEngineerId] = useState<number>(0)
    
    const [pdfData, setPdfData] = useState("");
    const [currentPage, setCurrentPage] = useState<number>(1)
    const [totalPageCount, setTotalPageCount] = useState<number>(0);

    const { user } = useAppSelector((state: { acount: any }) => state.acount)

  useEffect(() => {
        if (user?.userRole == "ROLE_PROJECT_MANAGER" || user?.userRole == "ROLE_ADMINISTRATOR"){
            getProject()
             getEmployees()
             return
        }
        navigate('/')
        return
        
    }, []);

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
        })
        .catch((error) => console.log(error))
  }

  const getEngineers = () => {
    agent.Engineer.getEngineers()
      .then((response) => {
        setEngineers(response)
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

  const handleEngineerId = (value: any) => {
    setEngineerId(value);
  }

  const getCV = (username: string) => {
    agent.Engineer.getCV(username)
      .then((response) => {
        const pdfBlob = new Blob([response], {type: 'application/pdf'});
        const pdfUrl = URL.createObjectURL(pdfBlob);
        setPdfData(pdfUrl);
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
            store.dispatch(refreshUser(user?.token));
            toast.info("Your token has been refreshed");
        }
        else if (error.response && error.response.status === 404){
            toast.info("No CV found");
        }
        else {
            console.log(error)
        }
    })
  }

  const prevPage = () => {
      if (currentPage == 1)
          return
      else
          setCurrentPage(currentPage - 1)
  }

  const nextPage = () => {
      if (currentPage == totalPageCount)
          return
      else
      setCurrentPage(currentPage + 1)
  }

  const onDocumentLoad = ({numPages} : {numPages: number}) => {
      setTotalPageCount(numPages);
  }

  const formatDate = (dateArray: number[]) => {
    const [year, month, day] = dateArray;
    const formattedDate = `${day < 10 ? "0" + day : day}.${month < 10 ? "0" + month : month}.${year}.`;
    return formattedDate;
  };


  const addEngineer = () => {
    if(engineerId == null){
      toast.error('invalid ID');
      return;
    }
    agent.Project.addEmployeeToProject(id, engineerId)
      .then(() => console.log('prosao'))
      .catch(error => console.log(error))
  }
  

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
        .then(() => toast.success('updated'))
        .catch(error => console.log(error))

    agent.Employee.updateEmployeeDescription(selectedEmployee?.id, description)
        .then(() => navigate('/manager'))
        .catch(error => console.log(error))
  }

    return (
        <>
        <Typography variant="h2" sx={{justifyContent: 'center', alignItems: 'center', mt: 5, mb: 5, textAlign: 'center'}}>{project?.name}</Typography>
        <Typography variant="h4" sx={{justifyContent: 'center', alignItems: 'center', mb: 3}}>Employees:</Typography>
        
        {user?.userRole == "ROLE_ADMINISTRATOR" &&
          <>
            <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="left">Engineer Id</TableCell>
                  <TableCell align="left"></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>                
                  <TableRow>
                    <TableCell component="th" scope="row">    
                      <TextField
                        variant="outlined"
                        value={engineerId}
                        onChange={(e) => handleEngineerId(e.target.value)}
                      />                 
                    </TableCell>   
                    <TableCell component="th" scope="row">    
                        <Button onClick={addEngineer} variant="contained" sx={{ml: 5}}>Add Engineer</Button>               
                    </TableCell>                  
                  </TableRow>                
              </TableBody>            
            </Table>
          </TableContainer>
          </>
        }

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
        
        {user?.userRole == "ROLE_ADMINISTRATOR" &&
            <></>
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
                      {user?.userRole == "ROLE_PROJECT_MANAGER" &&
                        <>
                          <Button variant="contained" onClick={() => {handleDesription(employee.description); setSelectedEmployee(employee); setIsUpdate(true); console.log(employee.startDate.toString())}}>Update</Button>
                          <Button variant="contained" sx={{mx: 1}} onClick={() => {getCV(employee.username);}}>View CV</Button>
                        </>
                      }
                      
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>}
              
            </Table>
          </TableContainer>
          <Grid sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
            {pdfData !== ""
            ?
                <>
                    <Grid>
                        <Typography variant="h6">CV</Typography> 
                        <Document file={pdfData} onLoadSuccess={onDocumentLoad}>
                                <Page pageNumber={currentPage}/>
                        </Document>
                        <Typography variant="h6" sx={{mb: 2}}>
                            {`Page: ${currentPage}/${totalPageCount}`}
                        </Typography>
                        <Grid sx={{mb: 2}}>
                          {currentPage > 1 ? <Button variant="outlined" onClick={prevPage} sx={{mr: 1}}>Previous</Button> : <></>}
                          {currentPage < totalPageCount ? <Button variant="outlined" onClick={nextPage} sx={{ml: 1}}>Next</Button> : <></>}
                        </Grid>
                        <Button variant="contained" color="error" onClick={() => setPdfData("")}>Close CV</Button>
                    </Grid>
                </>
            :<></>}
          </Grid>
        </>
        
    )
}