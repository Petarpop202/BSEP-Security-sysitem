import { createBrowserRouter } from "react-router-dom";
import Login from "../../features/account/Login";
import App from "../layout/App";
import Register from "../../features/account/Register";
import AdminAccept from "../../features/account/AdminAccept";
import PasswordlessLogin from "../../features/account/PasswordlessLogin";
//import Register from "../../features/account/Register";
//import App from "../layout/App";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            // {path: '', element: <Catalog />},
            {path: 'login', element: <Login />},
            {path: 'register', element: <Register />},
            {path: 'response', element: <AdminAccept />},
            {path: 'passwordless', element: <PasswordlessLogin/>}
        ]

    }
])