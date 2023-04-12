import { Box, Button, Grid, InputLabel, MenuItem, Paper, Select, Switch, TextField, Typography } from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import axios from "axios";
import { useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

export default function CreateCertificate() {
    
    const navigate = useNavigate();

    const [issuers, setIssuers] = useState<Map<string, string>>(new Map());
    const [selectedIssuerUID, setSelectedIssuerUID] = useState<string>("");
    const [selectedIssuer, setSelectedIssuer] = useState<string>("");
    
    useEffect(() => {
        axios.get("http://localhost:8080/certificate/issuers")
            .then(response => {
                setIssuers(response.data)
            })
            .catch(error => console.log(error));
    }, [])

    const [subject_commonName, setSubjectCommonName] = useState<string>("");
    const [subject_surname, setSubjectSurname] = useState<string>("");
    const [subject_givenName, setSubjectGivenName] = useState<string>("");
    const [subject_organization, setSubjectOrganization] = useState<string>("");
    const [subject_organizationalUnitName, setSubjectOrganizationalUnitName] = useState<string>("");
    const [subject_country, setSubjectCountry] = useState<string>("");
    const [subject_email, setSubjectEmail] = useState<string>("");

    const [isCA, setIsCA] = useState<boolean>(false);
    const [selfSigned, setSelfSigned] = useState<boolean>(false);

    const [certStartDate, setCertStartDate] = useState(new Date());
    const [certEndDate, setCertEndDate] = useState(new Date());

    const handleSubjectCommonNameChange = (event: any) => {
        setSubjectCommonName(event.target.value);
    };

    const handleSubjectSurnameChange = (event: any) => {
        setSubjectSurname(event.target.value);
    };

    const handleSubjectGivenNameChange = (event: any) => {
        setSubjectGivenName(event.target.value);
    };

    const handleSubjectOrganizationChange = (event: any) => {
        setSubjectOrganization(event.target.value);
    };

    const handleSubjectOrganizationalUnitNameChange = (event: any) => {
        setSubjectOrganizationalUnitName(event.target.value);
    };

    const handleSubjectCountryChange = (event: any) => {
        setSubjectCountry(event.target.value);
    };

    const handleSubjectEmailChange = (event: any) => {
        setSubjectEmail(event.target.value);
    };

    const handleSelectedIssuerChange = (event: any) => {
        setSelectedIssuer(event.target.value);
    }

    const handleMenuItemChange = (key: string) => {
        setSelectedIssuerUID(key);
    }

    const handleIsCAChange = () => {
        setIsCA(!isCA);
    };

    const handleIsSelfSignedChange = () => {
        setSelfSigned(!selfSigned);
    };

    const handleCertStartDateChange = (value : any) => {
        setCertStartDate(value);
    };

    const handleCertEndDateChange = (value : any) => {
        setCertEndDate(value);
    };

    const handleSubmit = (event : any) => {
        event.preventDefault();

        let subject = {
            commonName: subject_commonName,
            surname: subject_surname,
            givenName: subject_givenName,
            organization: subject_organization,
            organizationalUnitName: subject_organizationalUnitName,
            country: subject_country,
            email: subject_email,
        }

        let issuer = null
            
        let newCertificate = {
            subject: subject,
            issuer: issuer,
            issuerUID: "",
            isCA: isCA,
            isSelfSigned: selfSigned,
            startDate: certStartDate,
            endDate: certEndDate,
        }
        
        if (selectedIssuerUID.length){
            newCertificate.issuerUID = selectedIssuerUID;
        }

        axios.post("http://localhost:8080/certificate/create", newCertificate)
            .then(() => {
                navigate('/');
                toast.success("Successfully created a new certificate")
            })
            .catch(() => toast.error("Invalid arguments. Failed creating a new certificate"))
    };

    return (
        <Paper sx={{mb: 2, padding: 2}}>
            <Typography gutterBottom variant="h2">
                Create a new certificate
            </Typography>
            <form onSubmit={handleSubmit}>
                <Grid sx={{mb: 2, border: 1, borderColor: "gray", padding: 2}}>
                    <Typography variant="h4">Subject</Typography>
                    <TextField
                        label="Subject Common Name"
                        value={subject_commonName}
                        onChange={handleSubjectCommonNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Surname"
                        value={subject_surname}
                        onChange={handleSubjectSurnameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Given Name"
                        value={subject_givenName}
                        onChange={handleSubjectGivenNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Organization"
                        value={subject_organization}
                        onChange={handleSubjectOrganizationChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Organization Unit Name"
                        value={subject_organizationalUnitName}
                        onChange={handleSubjectOrganizationalUnitNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Country"
                        value={subject_country}
                        onChange={handleSubjectCountryChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Subject Email"
                        value={subject_email}
                        onChange={handleSubjectEmailChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                </Grid>
                <Grid container sx={{mb: 2, border: 1, borderColor: "gray", padding: 2}}>
                    <Grid item xs={12}>
                        <Box display='flex' alignItems='center'>
                            <Grid item xs={1.5}>
                                <Typography variant="h4">Issuer</Typography>
                            </Grid>
                            <Grid item xs={3.5}>
                                <Select
                                    value={selectedIssuer || ""}
                                    onChange={handleSelectedIssuerChange}
                                    fullWidth
                                    required = {!selfSigned}
                                    >
                                    {Object.entries(issuers).map(([key, value]) => {
                                        return <MenuItem
                                                    key = {key}
                                                    value={value}
                                                    onClick={() => handleMenuItemChange(key)}
                                                    >
                                                    {value}
                                                </MenuItem>
                                    })}
                                </Select>
                            </Grid>
                        </Box>
                    </Grid>
                </Grid>
                <Grid sx={{mb: 2, border: 1, borderColor: "gray", padding: 2}}>
                    <Grid container sx={{mt:2, mb: 2}}>
                        <Grid item xs={4.5}>
                            <Typography variant="h5">Certificate is self signed (is Root)</Typography>    
                        </Grid>
                        <Grid item>
                            <Switch checked={selfSigned} onChange={handleIsSelfSignedChange}/>
                        </Grid>
                    </Grid>
                    <Grid container sx={{mt:2, mb: 2}}>
                        <Grid item xs={4.5}>
                            <Typography variant="h5">Subject can sign certificates (is CA)</Typography>
                        </Grid>
                        <Grid item>
                            <Switch checked={isCA} onChange={handleIsCAChange}/>
                        </Grid>
                    </Grid>
                </Grid>
                <Grid sx={{mb: 2, border: 1, borderColor: "gray", padding: 2}}>
                <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DatePicker
                        label="Start Date"
                        value={certStartDate}
                        minDate={new Date()}
                        onChange={handleCertStartDateChange}
                        slotProps={{ textField: { fullWidth: true, margin: "normal", required: true } }}
                    />
                </LocalizationProvider>
                <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DatePicker
                        label="End Date"
                        value={certEndDate}
                        minDate={new Date()}
                        onChange={handleCertEndDateChange}
                        slotProps={{ textField: { fullWidth: true, margin: "normal", required: true} }}
                    />
                </LocalizationProvider>
                </Grid>
                <Button
                    type="submit"
                    variant="contained"
                    color="success"
                    size="large"
                    sx={{mb: 2, mt: 2}}
                    fullWidth
                >
                    Create
                </Button>
                <Button
                    component={NavLink}
                    to="/"
                    variant="contained"
                    color="error"
                    size="large"
                    sx={{mb: 2}}
                    fullWidth
                >
                    Cancel
                </Button>
            </form>
        </Paper>
    )
}