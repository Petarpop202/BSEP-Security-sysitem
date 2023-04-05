import { Button, Grid, Paper, TextField, Typography } from "@mui/material";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import axios from "axios";
import { useState } from "react";
import { toast } from "react-toastify";

export default function CreateCertificate() {
    
    const [subject_commonName, setSubjectCommonName] = useState<string>("");
    const [subject_surname, setSubjectSurname] = useState<string>("");
    const [subject_givenName, setSubjectGivenName] = useState<string>("");
    const [subject_organization, setSubjectOrganization] = useState<string>("");
    const [subject_organizationalUnitName, setSubjectOrganizationalUnitName] = useState<string>("");
    const [subject_country, setSubjectCountry] = useState<string>("");
    const [subject_email, setSubjectEmail] = useState<string>("");

    const [issuer_commonName, setIssuerCommonName] = useState<string>("");
    const [issuer_surname, setIssuerSurname] = useState<string>("");
    const [issuer_givenName, setIssuerGivenName] = useState<string>("");
    const [issuer_organization, setIssuerOrganization] = useState<string>("");
    const [issuer_organizationalUnitName, setIssuerOrganizationalUnitName] = useState<string>("");
    const [issuer_country, setIssuerCountry] = useState<string>("");
    const [issuer_email, setIssuerEmail] = useState<string>("");

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

    const handleIssuerCommonNameChange = (event: any) => {
        setIssuerCommonName(event.target.value);
    };

    const handleIssuerSurnameChange = (event: any) => {
        setIssuerSurname(event.target.value);
    };

    const handleIssuerGivenNameChange = (event: any) => {
        setIssuerGivenName(event.target.value);
    };

    const handleIssuerOrganizationChange = (event: any) => {
        setIssuerOrganization(event.target.value);
    };

    const handleIssuerOrganizationalUnitNameChange = (event: any) => {
        setIssuerOrganizationalUnitName(event.target.value);
    };

    const handleIssuerCountryChange = (event: any) => {
        setIssuerCountry(event.target.value);
    };

    const handleIssuerEmailChange = (event: any) => {
        setIssuerEmail(event.target.value);
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

        let issuer = {
            commonName: issuer_commonName,
            surname: issuer_surname,
            givenName: issuer_givenName,
            organization: issuer_organization,
            organizationalUnitName: issuer_organizationalUnitName,
            country: issuer_country,
            email: issuer_email,
        }

        let newCertificate = {
            subject: subject,
            issuer: issuer,
            startDate: certStartDate,
            endDate: certEndDate,
        }
        axios.post("http://localhost:8080/certificate/create", newCertificate)
            .then(() => {
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
                <Grid sx={{mb: 2, mt: 2, border: 1, borderColor: "gray", padding: 2}}>
                    <Typography variant="h4">Issuer</Typography>
                    <TextField
                        label="Issuer Common Name"
                        value={issuer_commonName}
                        onChange={handleIssuerCommonNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Surname"
                        value={issuer_surname}
                        onChange={handleIssuerSurnameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Given Name"
                        value={issuer_givenName}
                        onChange={handleIssuerGivenNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Organization"
                        value={issuer_organization}
                        onChange={handleIssuerOrganizationChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Organization Unit Name"
                        value={issuer_organizationalUnitName}
                        onChange={handleIssuerOrganizationalUnitNameChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Country"
                        value={issuer_country}
                        onChange={handleIssuerCountryChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                    <TextField
                        label="Issuer Email"
                        value={issuer_email}
                        onChange={handleIssuerEmailChange}
                        margin="normal"
                        fullWidth
                        required
                    />
                </Grid>
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