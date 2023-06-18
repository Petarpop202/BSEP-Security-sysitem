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

export default function ForgotPassword() {

    const dispatch = useAppDispatch()
    const navigate = useNavigate()
    const [username, setUsername] = useState('')

    const handleSubmit = () => {
        if(username === ''){
            return null
        }
        agent.Account.forgotPassword(username)
                    .then(() => {
                        toast.info("You will get e-mail to reset password !")
                        console.log(username)
                    })
                    .catch(() => {
                        toast.error('Invalid username!')
                    })}
    

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
        Reset password
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
          label="username"
          autoFocus
          onChange={(e) => setUsername(e.target.value)}
        />  
        <Button variant="contained" onClick={handleSubmit}>Confirm</Button>            
      </Box>
    </Container>
      );
}