import { Button, Menu, Fade, MenuItem } from "@mui/material"
import React, { useEffect, useState } from "react"
import { refreshUser, signOut } from "../../features/account/accountSlice"
import agent from "../api/agent"
import { store, useAppDispatch, useAppSelector } from "../apk/configureApk"
import { Link, Navigate, useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { router } from "../router/Router"

export default function SignedInMenu() {
  const dispatch = useAppDispatch()
  const navigate = useNavigate()
  const { user } = useAppSelector((state: { acount: any }) => state.acount)
  const Jwt = {
    refreshJwt: user?.refreshJwt,
    jwt: user?.jwt,
  }

  // const signOut = () => {
  //     localStorage.removeItem('user');
  //     location.reload();
  //     router.navigate('/');
  // }

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null)
  const open = Boolean(anchorEl)
  const handleClick = (event: any) => {
    setAnchorEl(event.currentTarget)
  }
  const handleRefresh = () => {
    agent.Account.getString()
      .then(() => {
        toast.info("Izvrseno")
      })
      .catch((error) => {
        if (error.response && error.response.status === 401) {
          store.dispatch(refreshUser(user?.token))
          toast.info("Your token has been refreshed")
        }
      })
  }
  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleProfile = () => {
    if (user.userRole === null) return
    if (user?.userRole === "ROLE_PROJECT_MANAGER") {
      router.navigate("/manager")
      handleClose()
    }
    handleClose()
  }

  return (
    <>
      <Button sx={{ typography: "h6" }} color="inherit" onClick={handleClick}>
        {user?.username}
      </Button>
      <Button sx={{ typography: "h6" }} color="inherit" onClick={handleRefresh}>
        Refresh
      </Button>
      <Menu
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        TransitionComponent={Fade}
      >
        <MenuItem onClick={handleProfile}>Profile</MenuItem>
        <MenuItem onClick={() => dispatch(signOut())}>Logout</MenuItem>
      </Menu>
    </>
  )
}
