import { Jwt } from "./Jwt";

export interface User {
    email: string;
    jwt: string;
    refreshJwt: string;
    userRole: string;
}