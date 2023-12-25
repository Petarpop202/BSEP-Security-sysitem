import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../app/apk/configureApk";
import { useEffect, useState } from "react";
import agent from "../../app/api/agent";
import { toast } from "react-toastify";
import { signInGuestUser, signInUser } from "./accountSlice";
import {
  Container,
  Paper,
  Avatar,
  Typography,
  Box,
  TextField,
  Grid,
  Button
} from "@mui/material"

export default function ResetPassword() {

    const dispatch = useAppDispatch()
    const navigate = useNavigate()
    const [password, setPassword] = useState('')
    const getParameterValueFromURL = (paramName: string): string | null => {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(paramName);
      };
      
    const username = getParameterValueFromURL('username');

    const handleSubmit = () => {
        if(username === ''){
            return null
        }
        if (password === "") {
          toast.error("Fields can not be empty!")
          return
        }
        if (password.length < 7) {
          toast.error("Password must contains at least 8 characters")
          return
        }
        agent.Account.resetPassword(username, password)
                    .then(() => {
                        toast.info("Password successfully changed!")
                        navigate('/')
                    })
                    .catch(() => {
                        toast.error('Invalid password!')
                    })
    }
    

      return (
        <Container
      component={Paper}
      maxWidth="sm"
      sx={{
        p: 4,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography component="h1" variant="h5">
        New password
      </Typography>
      <Box
        component="form"
        noValidate
        sx={{ mt: 1 }}    
      >
        <TextField
          margin="normal"
          required
          fullWidth
          label="new password"
          autoFocus
          type="password"
          onChange={(e) => setPassword(e.target.value)}
        />  
        <Button variant="contained" onClick={handleSubmit}>Change password</Button>            
      </Box>
    </Container>
      );
}