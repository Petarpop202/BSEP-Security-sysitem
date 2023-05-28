import { useNavigate } from "react-router";
import { useAppSelector } from "../../app/apk/configureApk";
import { useEffect, useState } from "react";
import { Role } from "../../app/models/Role";
import { Permission } from "../../app/models/Permission";
import agent from "../../app/api/agent";
import { Paper, Typography, Grid, Button } from "@mui/material";
import { Container, Box } from "@mui/system";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

export default function AdminPermissions() {
    const {user} = useAppSelector((state: { acount: any; }) => state.acount);

    const [roles, setRoles] = useState<Role[]>([])
    const [permissions, setPermissions] = useState<Permission[]>([])
    const [selectedRole, setSelectedRole] = useState<Role>()
    const [availablePermissions, setAvailablePermissions] = useState<Permission[]>([])
    const [selectedPermission, setSelectedPermission] = useState<Permission>()
    const navigate = useNavigate();

    useEffect(() => {
        if (user?.userRole !== "ROLE_ADMINISTRATOR" || user == null){
            navigate('/')
            return
        }
        getRolesAndPermissions() 
    }, []);

    const getRolesAndPermissions = () => {
        agent.Roles.getAllRoles()
            .then((response) =>{
                setRoles(response)
            })
            .catch((error) => console.log(error))
        agent.Roles.getAllPermissions()
            .then((response) => {
                setPermissions(response)
            })
            .catch((error) => console.log(error))
    }

    const editRole = (role: Role) => {
        setSelectedRole(role)
        getAvailablePermissions(role)
    }

    const getAvailablePermissions = (role: Role) => {
        let allPermissions = [...permissions]
        let rolePermissions = [...role.permissions]
        let difference = allPermissions.filter(item1 => !rolePermissions.some(item2 => item2.name === item1.name));
        setAvailablePermissions(difference)
    }

    const handleSelectChange = (event : any) => {
        const selectedPermissionName = availablePermissions.find(perm => perm.name === event.target.value);
        setSelectedPermission(selectedPermissionName)
    }

    const handleAddPermission = () => {
        if (selectedRole && selectedPermission){
            agent.Roles.addPermissionToRole(selectedRole.name, selectedPermission.name)
                .then((response) => {
                    getRolesAndPermissions()
                    setSelectedRole(undefined)
                    toast.success(`Successfully added permission ${selectedPermission.name} to role ${selectedRole.name}`)
                })
                .catch((error) => console.log(error))
        }
    }

    const handleRemovePermission = (permission : Permission) => {
        if (selectedRole){
            agent.Roles.removePermissionFromRole(selectedRole.name, permission.name)
                .then((response) => {
                    getRolesAndPermissions()
                    setSelectedRole(undefined)
                    toast.success(`Successfully removed permission ${permission.name} from role ${selectedRole.name}`)
                })
                .catch((error) => console.log(error))
        }
    }

    return (
        <Container component={Paper} maxWidth='sm' sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="div" variant="h3" sx={{ mb: 3 }}>
            Roles and Permissions
        </Typography>
        <hr/>
        <Box sx={{ width: '100%' }}>
            <Grid container sx={{mb: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography component="div" variant="h5" sx={{ mb: 3 }}>
                    Roles
                </Typography>
                <table style={{width: '100%', textAlign: 'center'}}>
                    <thead>
                        <th>Name</th>
                        <th>Action</th>
                    </thead>
                    <tbody>
                        {roles ? roles.map((role) => (
                            <tr key={role.id}>
                                <td>{role.name}</td>
                                <td><Button variant="contained" onClick={() => editRole(role)}>Edit</Button></td>
                            </tr>
                        )) : <></>}
                    </tbody>
                </table>
            </Grid>
            <hr/>
            {selectedRole && (<Grid container sx={{mb: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography component="div" variant="h5" sx={{ mb: 3 }}>
                    Permissions
                </Typography>
                <Grid item sx={{mb: 4}}>
                    <table style={{width: '100%', textAlign: 'center'}}>
                        <thead>
                            <th>Name</th>
                            <th>Action</th>
                        </thead>
                        <tbody>
                            {selectedRole ? selectedRole.permissions.map((permission) => (
                                <tr key={permission.id}>
                                    <td>{permission.name}</td>
                                    <td><Button variant="contained" color="error" onClick={() => handleRemovePermission(permission)}>Remove</Button></td>
                                </tr>
                            )) : <></>}
                        </tbody>
                    </table>
                </Grid>
                <Grid item sx={{mt: 4}}>
                    <select onChange={handleSelectChange}>
                        {availablePermissions.map(option => (
                        <option key={option.id} value={option.id}>
                            {option.name}
                        </option>
                        ))}
                    </select>
                    <Button
                        variant="contained"
                        color="success"
                        onClick={handleAddPermission}
                        >
                        Add
                    </Button>
                </Grid>
            </Grid>
            )}
        </Box>
        </Container>
    );
}