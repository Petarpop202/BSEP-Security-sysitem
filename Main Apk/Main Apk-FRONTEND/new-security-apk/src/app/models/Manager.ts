import { Address } from "./Address"

export interface Manager {
  id: number
  name: string
  surname: string
  blocked: boolean
  mail: string
  username: string
  phoneNumber: string
  jmbg: string
  address: Address
}
