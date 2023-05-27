import { useEffect, useState } from "react";
import { Container, Paper, Typography, Box, Grid, Button } from "@mui/material";
import agent from "../../app/api/agent";
import { useAppSelector } from "../../app/apk/configureApk";
import { useNavigate } from "react-router";
import { Link } from "react-router-dom";

export default function ProfileEngineer() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);

    const [id, setId] = useState();
    const [name, setName] = useState(user?.name ?? '');
    const [surname, setSurname] = useState(user?.surname ?? '');
    const [username, setUsername] = useState(user?.username ?? '');
    const [role, setRole] = useState(user?.userRole ?? '');
    const [jmbg, setJmbg] = useState(user?.jmbg ?? '');
    const [phoneNumber, setPhoneNumber] = useState(user?.phoneNumber ?? '');
    const [gender, setGender] = useState(user?.gender ?? 'MALE');
    const [streetNum, setStreetNum] = useState(user?.address?.streetNum ?? '');
    const [street, setStreet] = useState(user?.address?.street ?? '');
    const [city, setCity] = useState(user?.address?.city ?? '');
    const [country, setCountry] = useState(user?.address?.country ?? '');

    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ENGINEER" || user == null){
            navigate('/')
            return
        }

        agent.Engineer.getByUsername(user!.username)
            .then((response) =>{
                setId(response.id)
                setName(response.name)
                setSurname(response.surname)
                setUsername(response.username)
                setRole(response.userRole)
                setPhoneNumber(response.phoneNumber)
                setGender(response.gender)
                setJmbg(response.jmbg)
                setStreetNum(response.address?.streetNum)
                setStreet(response.address?.street)
                setCity(response.address?.city)
                setCountry(response.address?.country)
                setGender(response.gender)
            })
    }, []);

    const handleUpdateProfile = () => {
        let updatedEngineer = {
            id: id,
            username: username,
            name: name,
            surname: surname,
            phoneNumber: phoneNumber,
            jmbg: jmbg,
            gender: gender,
            address: {
                streetNum: streetNum,
                street: street,
                city: city,
                country: country
            }
        }
        agent.Engineer.updateEngineer(updatedEngineer)
            .then((response) => {
                console.log(response)
            })
            .catch((error) => console.log(error))
    };

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5" sx={{ mb: 3 }}>
            My Profile
        </Typography>
        <Box sx={{ width: '100%' }}>
            <Grid container spacing={3} sx={{ mb: 3 }}>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Username:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Name:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Surname:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={surname} onChange={(e) => setSurname(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Role:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                    <input type="text" value={role} onChange={(e) => setRole(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Phone Number:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Jmbg:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={jmbg} onChange={(e) => setJmbg(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Gender:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <select value={gender} onChange={(e) => setGender(e.target.value)} >
                    <option value = "MALE" >MALE</option>
                    <option value = "FEMALE" >FEMALE</option>
                </select>
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                        Address
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle2" sx={{ fontWeight: 'bold', textAlign: 'right' }}>
                        Street Number:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={streetNum} onChange={(e) => setStreetNum(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle2" sx={{ fontWeight: 'bold', textAlign: 'right' }}>
                        Street:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={street} onChange={(e) => setStreet(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle2" sx={{ fontWeight: 'bold', textAlign: 'right' }}>
                        City:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={city} onChange={(e) => setCity(e.target.value)} />
                </Grid>
                <Grid item xs={4}>
                    <Typography component="div" variant="subtitle2" sx={{ fontWeight: 'bold', textAlign: 'right' }}>
                        Country:
                    </Typography>
                </Grid>
                <Grid item xs={8}>
                <input type="text" value={country} onChange={(e) => setCountry(e.target.value)} />
                </Grid>
            </Grid>
            <Button
                variant="contained"
                sx = {{mt: 3, marginRight: '120px'}}
                onClick={handleUpdateProfile}
                >
                Edit Profile
            </Button>
            <Button
                component= {Link}
                variant="contained"
                sx = {{mt: 3, marginRight: '120px'}}
                to={'/skills'}
                >
                View Skills
            </Button>
        </Box>
        </Container>
    );
}
