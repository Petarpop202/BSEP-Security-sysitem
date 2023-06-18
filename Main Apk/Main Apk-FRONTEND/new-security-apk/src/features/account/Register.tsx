import { LockOutlined } from "@mui/icons-material";
import { LoadingButton } from "@mui/lab";
import { Container, Paper, Avatar, Typography, Box, TextField, Grid, Select, MenuItem, InputLabel } from "@mui/material";
import { useForm } from "react-hook-form";
import { useNavigate, Link } from "react-router-dom";
import { toast } from "react-toastify";
import agent from "../../app/api/agent";




export default function Register() {
    
    const navigate = useNavigate();
    const { register, handleSubmit, setError, formState: { isSubmitting, errors, isValid } } = useForm({
        mode: 'onTouched'
    })
    
    function handleApiErrors(errors: any) {
        console.log(errors);
        if (errors) {
            errors.forEach((error: string, index: number) => {
                if (error.includes('Password')) {
                    setError('password', { message: error })
                } else if (error.includes('Email')) {
                    setError('mail', { message: error })
                } else if (error.includes('Username')) {
                    setError('username', { message: error })
                }
            });
        }
    }

    function handlePassword(event: React.ChangeEvent<HTMLInputElement>) {
        const { name, value } = event.target;
        const form = event.target.form;

        if (form) {
          const passwordConfirmField = form.password.value; // Accessing the value of the password confirmation field
      
          if (value !== passwordConfirmField) {
            setError('passwordConfirm', {
              type: 'validate',
              message: 'Passwords do not match',
            });
          }else setError('passwordConfirm', {}, {shouldFocus:false});
        }
      }
    
    const options = [
        { 
          value: "MALE",
          label: "MALE"
        },
        {
          value:  "FEMALE",
          label:  "FEMALE"
        }
      ];

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                <LockOutlined />
            </Avatar>
            <Typography component="h1" variant="h5">
                Register
            </Typography>
            <Box component="form"
                noValidate sx={{ mt: 1 }}
                onSubmit={handleSubmit(data => {
                    agent.Account.register(data)
                    .then(() => {
                        toast.success('Registration successful - you can now login');
                        navigate('/login');
                    })
                    .catch(() => {
                        toast.error('Invalid parameters!')
                    })})}
            >
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Name"
                    autoFocus
                    {...register('name', { required: 'Name is required' })}
                    error={!!errors.name}
                    helperText={errors?.name?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Surname"
                    autoFocus
                    {...register('surname', { required: 'Surname is required' })}
                    error={!!errors.surname}
                    helperText={errors?.surname?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Jmbg"
                    autoFocus
                    {...register('jmbg', { required: 'Surname is required' })}
                    error={!!errors.jmbg}
                    helperText={errors?.jmbg?.message as string}
                />           
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Username"
                    autoFocus
                    {...register('username', { required: 'Username is required' })}
                    error={!!errors.username}
                    helperText={errors?.username?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Email"
                    {...register('mail', { required: 'Email is required' })}
                    error={!!errors.mail}
                    helperText={errors?.mail?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Password"
                    type="password"
                    {...register('password', { required: 'Password is required' })}
                    error={!!errors.password}
                    helperText={errors?.password?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Confirm Password"
                    type="password"
                    name="passwordConfirm"
                    //onInput={handlePassword}
                    error={!!errors.passwordConfirm}
                    helperText={errors?.passwordConfirm?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Country"
                    autoFocus
                    {...register('address.country', { required: 'Country is required' })}
                    // error={!!errors.address.country}
                    // helperText={errors?.username?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="City"
                    autoFocus
                    {...register('address.city', { required: 'City is required' })}
                    //error={!!errors?.address?.city}
                    //helperText={errors?.address?.city?.message || ''}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Street"
                    autoFocus
                    {...register('address.street', { required: 'Street is required' })}
                    // error={!!errors.username}
                    // helperText={errors?.username?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Street Number"
                    autoFocus
                    {...register('address.streetNum', { required: 'Street number is required' })}
                    // error={!!errors.username}
                    // helperText={errors?.username?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Phone number"
                    autoFocus
                    {...register('phoneNumber', { required: 'Phone number is required' })}
                    // error={!!errors.username}
                    // helperText={errors?.username?.message as string}
                />
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    label="Title"
                    autoFocus
                    {...register('title', { required: 'Title is required' })}
                    // error={!!errors.username}
                    // helperText={errors?.username?.message as string}
                />
                <InputLabel id="select1">Gender</InputLabel>
                <Select
                    id = "select1"
                    label="Gender"
                    placeholder="Gender"
                    //value={"Gender"}
                    fullWidth
                    {...register('gender', { required: 'Title is required' })}
                >
                <MenuItem value={"MALE"}>Male</MenuItem>
                <MenuItem value={"FEMALE"}>Female</MenuItem>
                
                </Select>
                <InputLabel id="select2">Role</InputLabel>
                <Select
                    id= "select2"
                    label="Role"
                    placeholder="Role"
                    //value={"Role"}
                    fullWidth
                    {...register('role', { required: 'Title is required' })}
                >
                <MenuItem value={"ROLE_HUMAN_RESOURCE_MANAGER"}>Manager of human resources</MenuItem>
                <MenuItem value={"ROLE_PROJECT_MANAGER"}>Project manager</MenuItem>
                <MenuItem value={"ROLE_ENGINEER"}>Engineer</MenuItem>
                </Select>                    
                                
                <LoadingButton loading={isSubmitting} type="submit"
                    disabled={!isValid}
                    fullWidth
                    variant="contained"                    
                    sx={{ mt: 3, mb: 2 }}>
                    Register
                </LoadingButton>

                <Grid container>
                    <Grid item>
                        <Link to='/login' style={{ textDecoration: 'none' }}>
                            {"Already have an account? Sign In"}
                        </Link>
                    </Grid>
                </Grid>
            </Box>
        </Container>
    );
}