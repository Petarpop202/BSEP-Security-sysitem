import { Jwt } from "./Jwt"

export interface User {
  id: string
  name: string
  surname: string
  username: string
  token: Jwt
  userRole: string
}
