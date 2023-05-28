import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Button, Table, TableBody, TableCell, TableContainer, TableRow, TextField } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { toast } from "react-toastify";

export default function ChangePassword() {

    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const [password, setPassword] = useState("")

    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ADMINISTRATOR" || user == null){
            navigate('/')
            return
        }
    }, []);

    const handleUpdatePassword = (newPassword: string) => {
        setPassword(newPassword)
    }

    const updatePassword = () => {
        if (password === "") {
          toast.error("Fields can not be empty!")
          return
        }
        if (password.length < 7) {
          toast.error("Password must contains at least 8 characters")
          return
        }
        agent.Administrator.updatePassword(Number(user.id), password)
          .then(() => {
            navigate('/')
            toast.success("Successfully updated!")
          })
          .catch((error) => console.log(error))
    }

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5" sx={{ mb: 3 }}>
            Change Password
        </Typography>
        <Box sx={{ width: '100%' }}>
            <TableContainer>
                <Table>
                <TableBody>
                    <TableRow>
                    <TableCell><Typography variant="subtitle1" sx={{ fontWeight: 'bold', textAlign: 'right' }}>New Password: </Typography></TableCell>
                    <TextField
                        variant="outlined"
                        type="password"
                        value={password}
                        onChange={(e) => handleUpdatePassword(e.target.value)}
                    />
                    </TableRow>
                </TableBody>
                </Table>
            </TableContainer>
            <Box textAlign="center">
                <Button
                    variant="contained"
                    color="success"
                    sx={{ ml: 13, mt: 2 }}
                    onClick={updatePassword}
                    >
                    Confirm
                </Button>
            </Box>
        </Box>
        </Container>
    );
}
