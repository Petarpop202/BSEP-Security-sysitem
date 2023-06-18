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
import ProfileAdmin from "../../features/admin/ProfileAdmin"
import CreateAdmin from "../../features/admin/CreateAdmin"
import AdminProjects from "../../features/admin/AdminProjects"
import NewProject from "../../features/admin/NewProject"
import ShowManagers from "../../features/admin/ShowManagers"
import ChangePassword from "../../features/admin/ChangePassword"
import ShowEmployees from "../../features/admin/ShowEmployees"
import UploadCV from "../../features/engineer/UploadCV"
import ManagerCVs from "../../features/hr-manager/ManagerCVs"
import LogsMonitoring from "../../features/admin/LogsMonitoring"
import AlarmsMonitoring from "../../features/admin/AlarmsMonitoring"
import GoogleAuthLogin from "../../features/account/GoogleAuthLogin"
import ForgotPassword from "../../features/account/ForgotPassword"
import ResetPassword from "../../features/account/ResetPassword"


export const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      { path: "login", element: <Login /> },
      { path: "register", element: <Register /> },
      { path: "response", element: <AdminAccept /> },
      { path: "passwordless", element: <PasswordlessLogin /> },
      { path: "profile-engineer", element: <ProfileEngineer /> },
      { path: "skills", element: <Skills /> },
      { path: "upload-cv", element: <UploadCV />},
      { path: "profile-manager", element: <ManagerProfilePage /> },
      { path: "manager", element: <ManagerProjects />},
      { path: "project-details/:id", element: <ProjectDetails />},
      { path: "engineer-projects", element: <EngineerProjects /> },
      { path: "guestlogin", element : <GuestLogged/> },
      { path: "roles", element: <AdminPermissions/> },
      { path: "profile-admin", element: <ProfileAdmin/> },
      { path: "admin-projects", element: <AdminProjects/> },
      { path: "new-project", element: <NewProject/> },
      { path: "show-managers", element: <ShowManagers/> },
      { path: "show-engineers", element: <ShowEmployees/> },
      { path: "create-admin", element: <CreateAdmin/> },
      { path: "change-password-admin", element: <ChangePassword/> },
      { path: "manager-cvs", element: <ManagerCVs/> },
      { path: "logs", element: <LogsMonitoring/> },
      { path: "alarms", element: <AlarmsMonitoring/>},
      { path: "googleAuthLogin", element: <GoogleAuthLogin/>},
      { path: "forgotPassword", element: <ForgotPassword/>},
      { path: "resetPassword", element: <ResetPassword/>}
    ],
  },
])
