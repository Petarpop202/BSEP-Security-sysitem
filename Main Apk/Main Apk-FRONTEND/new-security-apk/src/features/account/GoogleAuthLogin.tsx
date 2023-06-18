import { LockOutlined } from "@mui/icons-material"
import { LoadingButton } from "@mui/lab"
import {
  Container,
  Paper,
  Avatar,
  Typography,
  Box,
  TextField,
  Grid,
} from "@mui/material"
import { useForm } from "react-hook-form"
import { FieldValues } from "react-hook-form/dist/types"
import { useNavigate, Link, useLocation } from "react-router-dom"
import { toast } from "react-toastify"
import agent from "../../app/api/agent"
import { useAppDispatch } from "../../app/apk/configureApk"
import { signInUser } from "./accountSlice"
import { response } from "express"
import { DecodedToken } from "../../app/models/DecodedToken"
import { decodeToken } from "react-jwt"
import { request } from "http"

export default function GoogleAuthLogin() {
  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors, isValid },
  } = useForm({
    mode: "onTouched",
  })
  const dispatch = useAppDispatch()
  const navigate = useNavigate()
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const username = searchParams.get("username");
  const password = searchParams.get("password");

  var requestBody = {
    username: username,
    code: 0
  };

  const Login = () => {
    var data = {
        username: username,
        password: password
      };
    agent.Account.login(data)
    .then((response) => {
          navigate("/")
          dispatch(signInUser(response))
      })
      .catch(() => {
        toast.error("Invalid parameters!")
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
      <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
        <LockOutlined />
      </Avatar>
      <Typography component="h1" variant="h5">
        Sign in
      </Typography>
      <Box
        component="form"
        noValidate
        sx={{ mt: 1 }}
        onSubmit={handleSubmit((data) => {
          requestBody.code = data.code
          agent.Account.validateCode(requestBody)
            .then((response) => {
              if(response){
                Login();
                dispatch(signInUser(response))
                navigate("/")
              }
              else {
                toast.error("Invalid Code!")
              }
            })
            .catch(() => {
              toast.error("Invalid parameters!")
            }) 
        }
        )}
      >
        <TextField
          margin="normal"
          required
          fullWidth
          label="Code"
          autoFocus
          {...register("code", { required: "Code is required" })}
          error={!!errors.code}
          helperText={errors?.code?.message as string}
        />
        <LoadingButton
          loading={isSubmitting}
          type="submit"
          disabled={!isValid}
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
        >
          Sign in
        </LoadingButton>
      </Box>
    </Container>
  )
}