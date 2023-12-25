import { useEffect, useState } from "react"
import { Project } from "../../app/models/Project"
import agent from "../../app/api/agent"
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../app/apk/configureApk";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button} from '@mui/material';


export default function AdminProjects() {
  const navigate = useNavigate();
  const [projects, setProjects] = useState<Project[] | null>([])
  const { user } = useAppSelector((state: { acount: any }) => state.acount)

  useEffect(() => {
        if (user?.userRole !== "ROLE_ADMINISTRATOR" || user == null){
            navigate('/')
            return
        }
        getProjects() 
    }, []);

  const getProjects = () => {
    user?.id &&
      agent.Project.getProjects()
        .then((response) => {
          setProjects(response)
        })
        .catch((error) => console.log(error))
  }

  const formatDate = (dateArray: number[]) => {
    const [year, month, day] = dateArray;
    const formattedDate = `${day < 10 ? "0" + day : day}.${month < 10 ? "0" + month : month}.${year}.`;
    return formattedDate;
  };

  return (<TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Project name</TableCell>
                  <TableCell align="left">Start</TableCell>
                  <TableCell align="left">End</TableCell>
                  <TableCell align="center"></TableCell>
                </TableRow>
              </TableHead>
              {projects && <TableBody>
                {projects.map((project) => (
                  <TableRow
                    key={project.id}
                  >
                    <TableCell component="th" scope="row">
                      {project.name}
                    </TableCell>
                    <TableCell>
                      {project.startDate && formatDate(project.startDate)}
                    </TableCell>
                    <TableCell>
                      {project.endDate && formatDate(project.endDate)}
                    </TableCell>
                    <TableCell>
                      <Button variant="contained" onClick={() => navigate(`/project-details/${project.id}`)}>More</Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>}
              
            </Table>
          </TableContainer>)
}
