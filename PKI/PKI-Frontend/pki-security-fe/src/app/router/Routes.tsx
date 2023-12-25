import { createBrowserRouter } from "react-router-dom";
import App from "../layout/App";
import Certificates from "../../features/certificates/Certificates";
import CreateCertificate from "../../features/create-certificate/CreateCertificate";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {path: '', element: <Certificates/>},
            {path: 'new', element: <CreateCertificate/>},
        ]
    }
])