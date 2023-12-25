import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import agent from "../../app/api/agent"
import { useAppDispatch, useAppSelector } from "../../app/apk/configureApk"
import { Manager } from "../../app/models/Manager"
import {
  Divider,
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  Button,
  TextField,
  Box,
} from "@mui/material"
import { Input } from "@mui/icons-material"
import { Address } from "../../app/models/Address"
import { toast } from "react-toastify"
import { signOut } from "../account/accountSlice"

export default function ManagerProfilePage() {
  const navigate = useNavigate()
  const { user } = useAppSelector((state: { acount: any }) => state.acount)
  const [manager, setManager] = useState<Manager | null>(null)
  const [isUpdate, setIsUpdate] = useState(false)
  const [isChangePassword, setIsChangePassword] = useState(false)
  const [managerToUpdate, setManagerToUpdate] = useState<Manager | null>(null)
  const [password, setPassword] = useState("")
  const dispatch = useAppDispatch()

  const [address, setAddress] = useState<Address | null>(null)

  useEffect(() => {
    user?.id &&
      agent.Manager.currentManager(parseInt(user.id))
        .then((response) => {
          setManager(response)
          setManagerToUpdate(response)
          setAddress(response.address)
        })
        .catch((error) => console.log(error))
  }, [])

  if (user?.userRole.toUpperCase() !== "ROLE_PROJECT_MANAGER") {
    toast.error("Unauthorized user")
    return <h1>UNAUTHORIZED</h1>
  }

  if (!manager) return <h1>Manager not found!</h1>

  const handleChangeUpdate = () => {
    setIsUpdate(true)
  }

  const handleChangePassword = () => {
    setIsChangePassword(true)
  }

  const handleUpdateName = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          name: newName,
        }
      }
      return null
    })
  }

  const handleUpdateSurname = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          surname: newName,
        }
      }
      return null
    })
  }

  const handleUpdateMail = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          mail: newName,
        }
      }
      return null
    })
  }

  const handleUpdateUsername = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          username: newName,
        }
      }
      return null
    })
  }

  const handleUpdatePhoneNumber = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          phoneNumber: newName,
        }
      }
      return null
    })
  }

  const handleUpdateJmbg = (newName: string) => {
    setManagerToUpdate((prevManager) => {
      if (prevManager) {
        return {
          ...prevManager,
          jmbg: newName,
        }
      }
      return null
    })
  }

  const handleUpdateCountry = (newName: string) => {
    setAddress((prevAddress) => {
      if (prevAddress) {
        return {
          ...prevAddress,
          country: newName,
        }
      }
      return null
    })
  }

  const handleUpdateCity = (newName: string) => {
    setAddress((prevAddress) => {
      if (prevAddress) {
        return {
          ...prevAddress,
          city: newName,
        }
      }
      return null
    })
  }

  const handleUpdateStreet = (newName: string) => {
    setAddress((prevAddress) => {
      if (prevAddress) {
        return {
          ...prevAddress,
          street: newName,
        }
      }
      return null
    })
  }

  const handleUpdateStreetNum = (newName: string) => {
    setAddress((prevAddress) => {
      if (prevAddress) {
        return {
          ...prevAddress,
          streetNum: newName,
        }
      }
      return null
    })
  }

  const handleUpdateManagerAddress = () => {
    if (managerToUpdate && address != null) {
      setManagerToUpdate((prevManager) => {
        if (prevManager) {
          return {
            ...prevManager,
            address: {
              ...prevManager.address,
              ...address,
            },
          }
        }
        return null
      })
    }
  }

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
    agent.Manager.updatePassword(manager.id, password)
      .then(() => {
        toast.success("Successfully updated!")
        dispatch(signOut())
      })
      .catch((error) => console.log(error))
  }

  const updateManager = () => {
    if (
      managerToUpdate?.name === "" ||
      managerToUpdate?.surname === "" ||
      managerToUpdate?.mail === "" ||
      managerToUpdate?.username === "" ||
      managerToUpdate?.phoneNumber === "" ||
      managerToUpdate?.jmbg === "" ||
      address?.country === "" ||
      address?.city === "" ||
      address?.street === "" ||
      address?.streetNum === ""
    ) {
      toast.error("Fields can not be empty!")
      return
    }
    if (updateManager != null && address != null) {
      let newAddress = {
        country: address.country,
        city: address.city,
        street: address.street,
        streetNum: address.streetNum,
      }
      let newManager = {
        id: managerToUpdate?.id,
        name: managerToUpdate?.name,
        surname: managerToUpdate?.surname,
        mail: managerToUpdate?.mail,
        username: managerToUpdate?.username,
        phoneNumber: managerToUpdate?.phoneNumber,
        jmbg: managerToUpdate?.jmbg,
        address: newAddress,
      }
      agent.Manager.updateManager(newManager)
        .then(() => {
          toast.success("Successfully updated!")
          navigate("/profile-manager")
        })
        .catch((error) => console.log(error))
    } else {
      toast.error("Invalid parameters")
    }
  }

  return (
    <>
      <Typography variant="h3">{manager.name}</Typography>
      <Divider sx={{ mb: 2 }} />
      <TableContainer>
        <Table>
          <TableBody>
            <TableRow>
              <TableCell>Name</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.name}
                  onChange={(e) => handleUpdateName(e.target.value)}
                />
              ) : (
                <TableCell>{manager.name}</TableCell>
              )}
            </TableRow>
            <TableRow>
              <TableCell>Surname</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.surname}
                  onChange={(e) => handleUpdateSurname(e.target.value)}
                />
              ) : (
                <TableCell>{manager.surname}</TableCell>
              )}
            </TableRow>
            <TableRow>
              <TableCell>Email</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.mail}
                  onChange={(e) => handleUpdateMail(e.target.value)}
                />
              ) : (
                <TableCell>{manager.mail}</TableCell>
              )}
            </TableRow>
            <TableRow>
              <TableCell>Username</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.username}
                  onChange={(e) => handleUpdateUsername(e.target.value)}
                />
              ) : (
                <TableCell>{manager.username}</TableCell>
              )}
            </TableRow>
            <TableRow>
              <TableCell>Phone number</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.phoneNumber}
                  onChange={(e) => handleUpdatePhoneNumber(e.target.value)}
                />
              ) : (
                <TableCell>{manager.phoneNumber}</TableCell>
              )}
            </TableRow>
            <TableRow>
              <TableCell>Jmbg</TableCell>
              {isUpdate ? (
                <TextField
                  variant="outlined"
                  value={managerToUpdate?.jmbg}
                  onChange={(e) => handleUpdateJmbg(e.target.value)}
                />
              ) : (
                <TableCell>{manager.jmbg}</TableCell>
              )}
            </TableRow>
            {isUpdate ? (
              <>
                <TableRow>
                  <TableCell>Country</TableCell>
                  <TextField
                    variant="outlined"
                    value={address?.country}
                    onChange={(e) => handleUpdateCountry(e.target.value)}
                  />
                </TableRow>
                <TableRow>
                  <TableCell>City</TableCell>
                  <TextField
                    variant="outlined"
                    value={address?.city}
                    onChange={(e) => handleUpdateCity(e.target.value)}
                  />
                </TableRow>
                <TableRow>
                  <TableCell>Street</TableCell>
                  <TextField
                    variant="outlined"
                    value={address?.street}
                    onChange={(e) => handleUpdateStreet(e.target.value)}
                  />
                </TableRow>
                <TableRow>
                  <TableCell>Street number</TableCell>
                  <TextField
                    variant="outlined"
                    value={address?.streetNum}
                    onChange={(e) => handleUpdateStreetNum(e.target.value)}
                  />
                </TableRow>
              </>
            ) : (
              <TableRow>
                <TableCell>Address</TableCell>
                <TableCell>
                  {manager.address.city}, {manager.address.street}{" "}
                  {manager.address.streetNum}, {manager.address.country}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      {isUpdate ? (
        <Box>
          <Button
            variant="contained"
            sx={{ ml: 26, mt: 2 }}
            onClick={updateManager}
          >
            Update
          </Button>
          <Button
            variant="outlined"
            sx={{ ml: 10, mt: 2 }}
            onClick={() => setIsUpdate(!isUpdate)}
          >
            Cancel
          </Button>
        </Box>
      ) : (
        <></>
      )}
      {!isUpdate && !isChangePassword && (
        <Button
          variant="contained"
          sx={{ ml: 15, mt: 2 }}
          onClick={handleChangeUpdate}
        >
          Update
        </Button>
      )}
      {!isChangePassword && !isUpdate && (
        <Button
          variant="contained"
          sx={{ ml: 10, mt: 2 }}
          onClick={handleChangePassword}
        >
          Change Password
        </Button>
      )}
      {isChangePassword ? (
        <>
          <TableContainer>
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell>New Password</TableCell>

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
          <Box>
            <Button
              variant="contained"
              sx={{ ml: 26, mt: 2 }}
              onClick={updatePassword}
            >
              Update
            </Button>
            <Button
              variant="outlined"
              sx={{ ml: 10, mt: 2 }}
              onClick={() => {
                setIsUpdate(false)
                setIsChangePassword(false)
              }}
            >
              Cancel
            </Button>
          </Box>
        </>
      ) : (
        <></>
      )}
    </>
  )
}
