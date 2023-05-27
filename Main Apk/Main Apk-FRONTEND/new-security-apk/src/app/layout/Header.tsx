import { AirplaneTicket, ShoppingCart } from "@mui/icons-material";
import { AppBar, Badge, Box, IconButton, List, ListItem, Switch, Toolbar, Typography } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import agent from "../api/agent";
import { User } from "../models/User";
import { useAppSelector } from "../apk/configureApk";
import SignedInMenu from "./SignedInMenu";

const rightLinks = [
    {title: 'login', path: '/login'},
    {title: 'register', path: '/register'},
]

const navStyles = {
    color: 'inherit',
    textDecoration: 'none', 
    typography: 'h6',
    '&:hover': {
        color: 'grey.500'
    },
    '&.active':{
        color: 'text.secondary'
    }
}

interface Props {
    darkMode: boolean;
    handleThemeChange: () => void;
}

export default function Header({darkMode, handleThemeChange}: Props) {

    const [isLogged, setIsLogged] = useState<boolean>(false);
    const {user} = useAppSelector(state => state.acount);       

    return (
        <AppBar position='static' sx={{mb: 4}}>
            <Toolbar sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>

                <Box display='flex' alignItems='center'>
                    <Typography variant='h6' component={NavLink} 
                        to='/'
                        sx={navStyles}    
                    >
                        SECURITY-APK
                    </Typography>
                    <Switch checked={darkMode} onChange={handleThemeChange}/>
                </Box>

                <Box display='flex' alignItems='center'>
                    <IconButton component={NavLink} to='/myTickets' size='large' edge='start' color='inherit' sx={{mr: 2}}>
                            <Badge badgeContent='4' color="secondary">
                                <AirplaneTicket />
                            </Badge>
                    </IconButton>
                    
                    {user ? (
                        <SignedInMenu />
                        
                    ) : (
                        <List sx={{display: 'flex'}}>
                        {rightLinks.map(({title, path}) =>(
                            <ListItem
                                component={NavLink}
                                to={path}
                                key={path}
                                sx={navStyles}
                            >
                                {title.toUpperCase()}
                            </ListItem>
                        ))}
                        </List>
                    )}
                    
                    </Box>
            </Toolbar>
        </AppBar>
    )
}