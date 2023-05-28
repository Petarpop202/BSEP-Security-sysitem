import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../app/apk/configureApk";
import { useEffect } from "react";
import agent from "../../app/api/agent";
import { toast } from "react-toastify";
import { signInGuestUser, signInUser } from "./accountSlice";

export default function GuestLogged() {

    const dispatch = useAppDispatch()
    const navigate = useNavigate()
    const getParameterValueFromURL = (paramName: string): string | null => {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(paramName);
      };
      
      const token = getParameterValueFromURL('token');
      const refreshToken = getParameterValueFromURL('refreshToken');

      const jwt = {
        jwt: token,
        refreshJwt: refreshToken
      }

      useEffect(() => {
              dispatch(signInGuestUser(jwt))
              navigate("/")
      }, []);

      return (
        <button>Stranica</button>
      );
}