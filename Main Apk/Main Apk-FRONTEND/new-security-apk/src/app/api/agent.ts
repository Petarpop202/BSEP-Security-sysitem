import axios, { AxiosError, AxiosResponse } from "axios";
import { toast } from "react-toastify";
import { router } from "../router/Router";
import { store } from "../apk/configureApk";

axios.defaults.baseURL = 'https://localhost:8080/';
axios.defaults.withCredentials = true;

const responseBody = (response: AxiosResponse) => response.data;

axios.interceptors.request.use(config => {
    const accessToken = store.getState().acount.user?.jwt;
    //const refreshToken = store.getState().acount.user?.token.refreshToken;
    if (accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
    return config;
})

const requests = {
    get: (url: string) => axios.get(url).then(responseBody),
    post: (url: string, body: {}) => axios.post(url, body).then(responseBody),
    put: (url: string, body: {}) => axios.put(url, body).then(responseBody),
    delete: (url: string) => axios.delete(url).then(responseBody),
}

const Account = {
    login: (values: any) => requests.post('auth/login', values),
    register: (values: any) => requests.post('auth/register', values),
    refresh: (values: any) => requests.post('auth/refresh', values)
}


const agent = {
    Account
}

export default agent;