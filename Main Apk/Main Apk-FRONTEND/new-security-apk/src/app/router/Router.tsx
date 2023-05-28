import { createBrowserRouter } from "react-router-dom"
import Login from "../../features/account/Login"
import App from "../layout/App"
import Register from "../../features/account/Register"
import AdminAccept from "../../features/account/AdminAccept"
import PasswordlessLogin from "../../features/account/PasswordlessLogin"
import ProfileEngineer from "../../features/engineer/ProfileEngineer"
import Skills from "../../features/engineer/EngineerSkills"
import EngineerProjects from "../../features/engineer/EngineerProjects"
import ManagerPage from "../../features/manager/ManagerPage"

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
      { path: "manager", element: <ManagerPage /> },
      { path: "engineer-projects", element: <EngineerProjects /> },
    ],
  },
])
