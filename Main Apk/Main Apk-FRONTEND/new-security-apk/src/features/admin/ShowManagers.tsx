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
import { Manager } from "../../app/models/Manager";
import { toast } from "react-toastify";


export default function ShowManagers() {
  const navigate = useNavigate();
  const [managers, setManagers] = useState<Manager[] | null>([])
  const { user } = useAppSelector((state: { acount: any }) => state.acount)

  useEffect(() => {
        if (user?.userRole !== "ROLE_ADMINISTRATOR" || user == null){
            navigate('/')
            return
        }
        getManagers() 
    }, []);

    const getManagers = () => {
    user?.id &&
      agent.Manager.getManagers()
        .then((response) => {
          setManagers(response)
        })
        .catch((error) => console.log(error))
  }

  

  const formatDate = (dateArray: number[]) => {
    const [year, month, day] = dateArray;
    const formattedDate = `${day < 10 ? "0" + day : day}.${month < 10 ? "0" + month : month}.${year}.`;
    return formattedDate;
  };

  const handleBlock = (username: any) => {
    agent.Administrator.blockUser(username)
        .then(() => {
            toast.success("User successfully blocked!")
        })
        .catch(() => {
            toast.error('error!')
        })
  }
  const handleUnblock = (username: any) => {
    agent.Administrator.unblockUser(username)
        .then(() => {
            toast.success("User successfully unblocked!")
        })
        .catch(() => {
            toast.error('error!')
        })
  }

  return (<TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell align="left">Name</TableCell>
                  <TableCell align="left">Surname</TableCell>
                  <TableCell align="left">Mail</TableCell>
                  <TableCell align="left">Username</TableCell>
                  <TableCell align="left">Phone number</TableCell>
                  <TableCell align="left">Address</TableCell>
                  <TableCell align="left"></TableCell>
                </TableRow>
              </TableHead>
              {managers && <TableBody>
                {managers.map((manager) => (
                  <TableRow
                    key={manager.id}
                  >
                    <TableCell component="th" scope="row">
                      {manager.name}
                    </TableCell>
                    <TableCell>
                      {manager.surname}
                    </TableCell>
                    <TableCell>
                      {manager.mail}
                    </TableCell>
                    <TableCell>
                      {manager.username}
                    </TableCell>
                    <TableCell>
                      {manager.phoneNumber}
                    </TableCell>
                    <TableCell>
                      {manager.address.city} {manager.address.street} {manager.address.streetNum}
                    </TableCell>   
                    {manager.blocked === false &&
                    <TableCell>
                      <Button onClick={() => handleBlock(manager.username)} variant="outlined" color="error" sx={{ml: 5}}>Block</Button>
                    </TableCell>
                  }
                  {manager.blocked &&
                    <TableCell>
                      <Button onClick={() => handleUnblock(manager.username)} variant="outlined" sx={{ml: 5}}>Unblock</Button>
                    </TableCell>
                  }                 
                    
                  </TableRow>
                ))}
              </TableBody>}
              
            </Table>
          </TableContainer>)
}
