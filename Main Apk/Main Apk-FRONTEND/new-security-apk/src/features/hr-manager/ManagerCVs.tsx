import { useEffect, useState } from "react"
import { store, useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router-dom";
import agent from "../../app/api/agent";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button, Typography, Grid} from '@mui/material';
import { toast } from "react-toastify";
import { Manager } from "../../app/models/Manager";
import { refreshUser } from "../account/accountSlice";
import { Document, Page, pdfjs } from "react-pdf/dist/esm";
import 'react-pdf/dist/esm/Page/AnnotationLayer.css';
import 'react-pdf/dist/esm/Page/TextLayer.css';

export default function ManagerCVs () {
    pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

    const navigate = useNavigate();
    const [engineers, setEngineers] = useState<Manager[] | null>([])
    
    const [pdfData, setPdfData] = useState("");
    const [currentPage, setCurrentPage] = useState<number>(1)
    const [totalPageCount, setTotalPageCount] = useState<number>(0);

    const { user } = useAppSelector((state: { acount: any }) => state.acount)

    useEffect(() => {
        if (user?.userRole == "ROLE_HUMAN_RESOURCE_MANAGER" || user?.userRole == "ROLE_ADMINISTRATOR"){
            getEngineers();
            return
        }
        navigate('/')
        return
        
    }, []);

    const getEngineers = () => {
        agent.Engineer.getEngineers()
        .then((response) => {
            setEngineers(response)
            console.log(response)
        })
        .catch((error) => {
            if (error.response && error.response.status === 401) {
                store.dispatch(refreshUser(user?.token));
                toast.info("Your token has been refreshed");
            }
        })
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

    return (
        <>
        <Typography variant="h4" sx={{justifyContent: 'center', alignItems: 'center', mb: 3}}>
            Engineers:
        </Typography>
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
              <TableHead>
                <TableRow>
                  <TableCell>Name</TableCell>
                  <TableCell>Surname</TableCell>
                  <TableCell>Action</TableCell>
                </TableRow>
              </TableHead>
              {engineers && <TableBody>
                {engineers.map((engineer) => (
                  <TableRow
                    key={engineer.id}
                  >
                    <TableCell component="th" scope="row">
                      {engineer.name}
                    </TableCell>
                    <TableCell>
                      {engineer.surname}
                    </TableCell>
                    <TableCell>
                        <Button variant="contained" onClick={() => {getCV(engineer.username);}}>View CV</Button>
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