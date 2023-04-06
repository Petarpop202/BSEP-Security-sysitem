import { Card, CardContent, Container, Grid, Typography } from "@mui/material"

interface Props {
    certificate: any
}

export default function CertificateCard({certificate} : Props) {

    return (
        <>
            <Card>
                <CardContent>
                    <Grid sx={{mb: 2}}>
                        <Typography>
                            Version: {certificate.version}
                        </Typography>
                        <Typography>
                            Serial Number: {certificate.serialNumber}
                        </Typography>
                        <Typography>
                            Signature OID: {certificate.signatureOid}
                        </Typography>
                        {/*
                        <Typography>
                            Signature: {certificate.signature}
                        </Typography>
                         */}
                        <Typography>
                            Start Date: <b>{certificate.validity.notBefore.toLocaleDateString()}</b>
                            at <b>{certificate.validity.notBefore.toLocaleTimeString()}</b>
                        </Typography>
                        <Typography>
                            End Date: <b>{certificate.validity.notAfter.toLocaleDateString()}</b>
                            at <b>{certificate.validity.notAfter.toLocaleTimeString()}</b>
                        </Typography>
                    </Grid>
                    <Grid container>
                        <Grid item xs={6}>
                        <Typography variant="h5">
                            Subject
                        </Typography>
                        <Container>
                            <Typography>
                                Common Name: {certificate.subject.attributes[0].value}
                            </Typography>
                            <Typography>
                                Surname: {certificate.subject.attributes[1].value}
                            </Typography>
                            <Typography>
                                Given Name: {certificate.subject.attributes[2].value}
                            </Typography>
                            <Typography>
                                Organization: {certificate.subject.attributes[3].value}
                            </Typography>
                            <Typography>
                                Organizational Unit Name: {certificate.subject.attributes[4].value}
                            </Typography>
                            <Typography>
                                Country: {certificate.subject.attributes[5].value}
                            </Typography>
                            <Typography>
                                Email: {certificate.subject.attributes[6].value}
                            </Typography>
                            <Typography>
                                UID: {certificate.subject.attributes[7].value}
                            </Typography>
                        </Container>
                        </Grid>

                        <Grid item xs={6}>
                        <Typography variant="h5">
                            Issuer
                        </Typography>
                        <Container>
                            <Typography>
                                Common Name: {certificate.issuer.attributes[0].value}
                            </Typography>
                            <Typography>
                                Surname: {certificate.issuer.attributes[1].value}
                            </Typography>
                            <Typography>
                                Given Name: {certificate.issuer.attributes[2].value}
                            </Typography>
                            <Typography>
                                Organization: {certificate.issuer.attributes[3].value}
                            </Typography>
                            <Typography>
                                Organizational Unit Name: {certificate.issuer.attributes[4].value}
                            </Typography>
                            <Typography>
                                Country: {certificate.issuer.attributes[5].value}
                            </Typography>
                            <Typography>
                                Email: {certificate.issuer.attributes[6].value}
                            </Typography>
                            <Typography>
                                UID: {certificate.issuer.attributes[7].value}
                            </Typography>
                        </Container>
                        </Grid>
                    </Grid>
                </CardContent>
            </Card>
        </>
    )
}