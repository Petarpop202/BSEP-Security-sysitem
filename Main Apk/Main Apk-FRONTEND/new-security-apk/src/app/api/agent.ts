import axios, { AxiosResponse } from "axios"
import { store } from "../apk/configureApk"
import { isExpired } from "react-jwt"

axios.defaults.baseURL = "https://localhost:8080/"
axios.defaults.withCredentials = true

const responseBody = (response: AxiosResponse) => response.data

axios.interceptors.request.use(async (config) => {
  const accessToken = store.getState().acount.user?.token?.jwt
  const refreshToken = store.getState().acount.user?.token?.refreshJwt
  const is = isExpired(accessToken as string)
  if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`
  return config
})

const requests = {
  get: (url: string) => axios.get(url).then(responseBody),
  post: (url: string, body: {}) => axios.post(url, body).then(responseBody),
  put: (url: string, body: {}) => axios.put(url, body).then(responseBody),
  delete: (url: string) => axios.delete(url).then(responseBody),
}

const Account = {
    login: (values: any) => requests.post('auth/login', values),
    register: (values: any) => requests.post('auth/signup', values),
    refresh: (values: any) => requests.post('auth/refresh', values),
    getString: () => requests.get('auth/getString'),
    getRequests: () => requests.get('auth/getRequests'),
    response: (values: any) => requests.put('auth/response', values),
    passwordlessLogin: (values: any) => requests.post('auth/passwordlessLogin', { mail: values })
}

const Engineer = {
    getByUsername: (username: any) => requests.get(`engineers/username=${username}`),
    getById: (id: any) => requests.get(`engineers/${id}`),
    updateEngineer: (values: any) => requests.put('engineers', values),
    updatePassword: (id: number, values: any) => requests.put(`engineers/${id}`, values),
    updateEngineerSkills: (values: any) => requests.put('engineers/skills-update', values),
    getEngineers: () => requests.get('engineers'),
    uploadCV: (file: FormData) => axios.post(`engineers/upload-cv`, file, { headers:{"Content-Type": "multipart/form-data"}}).then(responseBody),
    getCV: (username: string) => axios({url: `engineers/get-cv/${username}`, method: 'GET', responseType: 'arraybuffer'}).then(responseBody),
    downloadCV: (username: string) => axios({url: `engineers/download-cv/${username}`, method: 'GET', responseType: 'blob'}).then(responseBody),
  }

const Employee = {
    getProjectsByEmployeeId : (id: any) => requests.get(`employees/${id}/projects`),
    getEmployeesByEngineerId : (id: any) => requests.get(`employees/engineer=${id}`),
    updateEmployeeDescription : (id: any, body: any) => requests.put(`employees/${id}/description`, body), 
    getEmployeesByProjectId : (id: any) => requests.get(`employees/project-id/${id}`),
    updateEmployee : (value: any) => requests.put('employees', value),
}

const Manager = {
  currentManager: (id: number) => requests.get(`managers/${id}`),
  updateManager: (values: any) => requests.put("managers", values),
  updatePassword: (id: number, values: any) =>
    requests.put(`managers/${id}`, values),
  getManagerProjects: (id: number) => requests.get(`projects/manager/${id}`),
  getManagers: () => requests.get('managers'),
}

const Project = {
  getProjectById : (id: any) => requests.get(`projects/${id}`),
  getProjects: () => requests.get('projects'),
  createProject: (values: any) => requests.post('projects', values),
  addEmployeeToProject: (projectId: any, employeeId: any) => requests.post(`projects/${projectId}/employee=${employeeId}`, {}),
}

const Roles = {
    getAllRoles: () => requests.get('roles'),
    getAllPermissions: () => requests.get('roles/permissions'),
    addPermissionToRole: (role : string, permission: string) => requests.post(`roles/${role}/permission=${permission}`, {}),
    removePermissionFromRole: (role : string, permission: string) => requests.delete(`roles/${role}/permission=${permission}`),
}

const Administrator = {
    getById: (id: any) => requests.get(`system-administrators/${id}`),
    updateAdministrator: (values : any) => requests.put('system-administrators', values),
    updatePassword: (id: number, values: any) => requests.put(`system-administrators/${id}`, values),
}

const agent = {
    Account,
    Engineer,
    Employee,
    Manager,
    Project,
    Roles,
    Administrator
}

export default agent
