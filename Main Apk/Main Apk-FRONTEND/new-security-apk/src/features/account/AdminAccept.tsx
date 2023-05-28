import { Grid, Paper, Typography, Button } from "@mui/material";
import { Box, Container } from "@mui/system";
import { store, useAppSelector } from "../../app/apk/configureApk";
import { useEffect, useState } from "react";
import agent from "../../app/api/agent";
import { RegistrationRequest } from "../../app/models/RegistrationRequest";
import { refreshUser } from "./accountSlice";
import { toast } from "react-toastify";
import { ResponseDTO } from "../../app/models/ResponseDTO";

export default function AdminAccept() {
  const { user } = useAppSelector((state: { acount: any }) => state.acount);
  const [registrationRequests, setRegistrationRequests] = useState<
    RegistrationRequest[]
  >([]);

  useEffect(() => {
    agent.Account.getRequests()
      .then((response) => {
        setRegistrationRequests(response);
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          store.dispatch(refreshUser(user?.token));
          toast.info("Your token has been refreshed");
        }
      });
  }, []);

  const handleAccept = (reqquestId: string) => {
    const responseDto: ResponseDTO = {
        requestId: reqquestId,
        response: true
      };
    
      agent.Account.response(responseDto)
        .then(() => {
          toast.info("Request accepted");
        })
        .catch((error) => {
          // Obrada greške pri prihvatanju zahtjeva
        });
  };

  const handleDecline = (reqquestId: string) => {
    const responseDto: ResponseDTO = {
        requestId: reqquestId,
        response: false
      };
    
      agent.Account.response(responseDto)
        .then(() => {
          toast.info("Request denied");
        })
        .catch((error) => {
          // Obrada greške pri prihvatanju zahtjeva
        });
  };

  return (
    <Container
      component={Paper}
      maxWidth="sm"
      sx={{ p: 4, display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <Typography component="div" variant="h3" sx={{ mb: 3 }}>
        Requests
      </Typography>
      <Box sx={{ width: "100%" }}>
        <Grid container sx={{ mb: 4 }}>
          <table style={{ width: "100%", textAlign: "center" }}>
            <thead>
              <tr>
                <th>Id</th>
                <th>User username</th>
                <th>Accept</th>
                <th>Denied</th>
              </tr>
            </thead>
            <tbody>
              {registrationRequests.map((request) => (
                <tr key={request.id}>
                  <td>{request.id}</td>
                  <td>{request.userUsername}</td>
                  <td><Button onClick={() => handleAccept(request.id)}>Accept</Button></td>
                  <td><Button onClick={() => handleDecline(request.id)}>Decline</Button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </Grid>
      </Box>
    </Container>
  );
}