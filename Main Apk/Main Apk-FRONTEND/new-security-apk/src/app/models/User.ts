import { Jwt } from "./Jwt";

export interface User {
    name: string;
    surname: string;
    username: string;
    token: Jwt;
    userRole: string;
}