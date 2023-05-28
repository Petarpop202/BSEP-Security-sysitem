import { createBrowserRouter } from "react-router-dom"
import Login from "../../features/account/Login"
import App from "../layout/App"
import Register from "../../features/account/Register"
import AdminAccept from "../../features/account/AdminAccept"
import PasswordlessLogin from "../../features/account/PasswordlessLogin"
import ProfileEngineer from "../../features/engineer/ProfileEngineer"
import Skills from "../../features/engineer/EngineerSkills"
import EngineerProjects from "../../features/engineer/EngineerProjects"
import ManagerProfilePage from "../../features/manager/ManagerProfilePage"
import ManagerProjects from "../../features/manager/ManagerProjects"
import ProjectDetails from "../../features/manager/ProjectDetails"
import GuestLogged from "../../features/account/GuestLogged"
import AdminPermissions from "../../features/admin/AdminPermissions"


export const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      // {path: '', element: <Catalog />},
      { path: "login", element: <Login /> },
      { path: "register", element: <Register /> },
      { path: "response", element: <AdminAccept /> },
      { path: "passwordless", element: <PasswordlessLogin /> },
      { path: "profile-engineer", element: <ProfileEngineer /> },
      { path: "skills", element: <Skills /> },
      { path: "profile-manager", element: <ManagerProfilePage /> },
      { path: "manager", element: <ManagerProjects />},
      { path: "project-details/:id", element: <ProjectDetails />},
      { path: "engineer-projects", element: <EngineerProjects /> },
      { path: "guestlogin", element : <GuestLogged/> },
      { path: "roles", element: <AdminPermissions/> }
    ],
  },
])
