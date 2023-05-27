import { Button, Menu, Fade, MenuItem } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { signOut } from "../../features/account/accountSlice";
import agent from "../api/agent";
import { User } from "../models/User";
import { router } from "../router/Router";
import { useAppDispatch, useAppSelector } from "../apk/configureApk";
import { Navigate, useNavigate } from "react-router-dom";




export default function SignedInMenu() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);
    const Jwt = {
        refreshJwt: user?.refreshJwt,
        jwt: user?.jwt
    }
    
    // const signOut = () => {
    //     localStorage.removeItem('user');
    //     location.reload();        
    //     router.navigate('/');        
    // }
    
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const open = Boolean(anchorEl);
    const handleClick = (event: any) => {
        setAnchorEl(event.currentTarget);
    };
    const handleRefresh = () => {
        agent.Account.refresh(Jwt).then(()=>
        navigate('/'));
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <>
            <Button
                sx={{typography: 'h6'}}
                color='inherit'
                 onClick={handleClick}>
                {user?.email}
            </Button>
            <Button
                sx={{typography: 'h6'}}
                color='inherit'
                 onClick={handleRefresh}>
                Refresh
            </Button>
            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                TransitionComponent={Fade}
            >
                <MenuItem onClick={handleClose}>Profile</MenuItem>
                <MenuItem onClick={() => dispatch(signOut())}>Logout</MenuItem>
            </Menu>
        </>
    );
    
}

