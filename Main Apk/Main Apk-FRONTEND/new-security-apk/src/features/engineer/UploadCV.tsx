import { Container, Paper, Typography, Box, Grid, Button } from "@mui/material";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import agent from "../../app/api/agent";
import { store, useAppSelector } from "../../app/apk/configureApk";
import { refreshUser } from "../account/accountSlice";
import { Link, useNavigate } from "react-router-dom";
import { Document, Page, pdfjs } from "react-pdf/dist/esm";
import 'react-pdf/dist/esm/Page/AnnotationLayer.css';
import 'react-pdf/dist/esm/Page/TextLayer.css';

export default function UploadCV() {
    pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;
    
    const [pdfData, setPdfData] = useState("");
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [selectedFile, setSelectedFile] = useState();
    const [currentPage, setCurrentPage] = useState<number>(1)
    const [totalPageCount, setTotalPageCount] = useState<number>(0);
    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ENGINEER" || user == null){
            navigate('/')
            return
        }
    }, []);

    useEffect(() => {
        agent.Engineer.getCV(user?.username)
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
    }, [])

    const download = () => {
        agent.Engineer.downloadCV(user?.username)
            .then((response) => {
                const href = URL.createObjectURL(response);
                const link = document.createElement('a');
                link.href = href;
                link.setAttribute('download', `${user.username}_CV.pdf`);
                document.body.appendChild(link);
                link.click();

                document.body.removeChild(link);
                URL.revokeObjectURL(href);
            })
            .catch((error) => {
                console.log(error)
                if (error.response && error.response.status === 401) {
                    store.dispatch(refreshUser(user?.token));
                    toast.info("Your token has been refreshed");
                }
            })
    }

    const selectFile = (event: any) => {
        setSelectedFile(event.target.files[0]);
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

    const upload = () => {
        let newFile = new FormData();
        newFile.append("file", selectedFile!)

        agent.Engineer.uploadCV(newFile)
            .then((response) => {
                toast.success(`Successfully uploaded the file: ${response}`);
             })
            .catch((error) => {
                console.log(error)
                toast.error(`Failed to upload file! ${error.response?.status === 400 ? error.response?.data : ""}`);
                if (error.response && error.response.status === 401) {
                    store.dispatch(refreshUser(user?.token));
                    toast.info("Your token has been refreshed");
                  }
            });
    }

    return (
        <Container component={Paper} maxWidth='md' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Typography component="h1" variant="h4" sx={{ mb: 3 }}>
                My CV
            </Typography>
            <Box sx={{ width: '100%', textAlign: 'center' }}>
                <Grid>
                    <input type="file" accept="application/pdf" onChange={selectFile}/>
                </Grid>
                <Grid>
                    <Button variant="contained" disabled={!selectedFile} onClick={upload} type="submit">Upload</Button>
                </Grid>
                <hr/>
                <Grid>
                    {pdfData !== ""
                    ?
                        <>
                            <Grid>
                                <Typography variant="h6">Currently uploaded CV</Typography> 
                                <Document file={pdfData} onLoadSuccess={onDocumentLoad}>
                                        <Page pageNumber={currentPage}/>
                                </Document>
                                <Typography variant="h6" sx={{mb: 2}}>
                                    {`Page: ${currentPage}/${totalPageCount}`}
                                </Typography>
                                {currentPage > 1 ? <Button variant="outlined" onClick={prevPage} sx={{mr: 1}}>Previous</Button> : <></>}
                                {currentPage < totalPageCount ? <Button variant="outlined" onClick={nextPage} sx={{ml: 1}}>Next</Button> : <></>}
                            </Grid>
                            <Grid>
                                <Button variant="contained" onClick={download} sx={{mt: 4, mb: 2}}>Download file</Button>
                            </Grid>
                            <hr/> 
                        </>
                    :
                    <Typography variant="h6">
                        No CV was uploaded
                    </Typography>}
                    <Button component={Link} variant="contained" color="error" to="/profile-engineer" sx={{mt: 4}}>Back</Button>
                </Grid>
            </Box>
        </Container>
    );
}