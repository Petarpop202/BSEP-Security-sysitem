import { createBrowserRouter } from "react-router-dom";
import Login from "../../features/account/Login";
import App from "../layout/App";
import ProfileEngineer from "../../features/engineer/ProfileEngineer";
import Skills from "../../features/engineer/EngineerSkills";
import EngineerProjects from "../../features/engineer/EngineerProjects";
//import Register from "../../features/account/Register";
//import App from "../layout/App";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            // {path: '', element: <Catalog />},
            {path: 'login', element: <Login />},
            //{path: 'register', element: <Register />},
            {path: 'profile-engineer', element: <ProfileEngineer />},
            {path: 'skills', element: <Skills />},
            {path: 'engineer-projects', element: <EngineerProjects/>}
        ]
    }
])