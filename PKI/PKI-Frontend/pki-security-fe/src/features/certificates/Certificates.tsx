import { Button, Grid, Typography } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import CertificateCard from "./CertificateCard";

export default function Certificates() {

    const forge = require("node-forge")

    const[certificates, setCertificates] = useState<any[]>([]);

    useEffect(() => {
        axios.get("http://localhost:8080/certificate")
            .then(response => setCertificates(response.data))
            .catch(error => console.log(error));
    }, [])

    return (
        <>
            <Button
                component={NavLink}
                to="/new"
                variant="contained"
                size="large"
                fullWidth
            >
                Create new certificate
            </Button>
            <Typography variant="h2">List of certificates</Typography>
            <Grid container>
                {
                    certificates.map(cert => {
                        return <Grid item key={cert} xs={12} sx={{mb: 2}}>{
                            <>
                                <CertificateCard certificate={forge.pki.certificateFromPem(cert)}/>
                            </>
                        }</Grid>
                    })
                }
            </Grid>
        </>
    )
}