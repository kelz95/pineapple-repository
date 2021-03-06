import axios, { AxiosError, AxiosResponse } from "axios";

import asyncWrapper from "../../lib/asyncWrapper";
import { BASE_URL_API } from "../../lib/constants";
import { Role } from "./types";
import { useAuthStore } from "./useAuthStore";

const authRequest = axios.create({
  baseURL: `${BASE_URL_API}/api/v1/auth`,
});

type SignInRequest = {
  username: string;
  password: string;
};

type SignInResponse = {
  id: number;
  accessToken: string;
  type: string; // "Bearer"
  username: string;
  roles: Role[];
};

type SignInError = {
  codigo: number;
  mensaje: string;
};

type RequestRecoverPasswordRequest = string;

// type RequestRecoverPasswordRequest = {
//   parametro: string;
// };

type RequestRecoverPasswordResponse = {
  codigo: number;
  mensaje: string;
};

type RequestRecoverPasswordError = {
  codigo: number;
  mensaje: string;
};

type RecoverPasswordRequest = {
  username: string;
  password: string;
};

class AuthController {
  static async signIn(payload: SignInRequest) {
    const [res, err] = await asyncWrapper<AxiosResponse<SignInResponse>, AxiosError<SignInError>>(
      authRequest.post("/signin", payload)
    );
    if (err || !res) {
      return { data: null, error: err?.response?.data.mensaje || err?.message };
    }
    useAuthStore.getState().setUser({
      id: res.data.id,
      roles: res.data.roles,
      username: res.data.username,
    });
    useAuthStore.getState().setAccessToken(res.data.accessToken);

    return { data: res.data, error: null };
  }

  static async signOut() {
    useAuthStore.getState().nullify();
  }

  static async requestRestorePassword(payload: RequestRecoverPasswordRequest) {
    const [res, err] = await asyncWrapper<
      AxiosResponse<RequestRecoverPasswordResponse>,
      AxiosError<RequestRecoverPasswordError>
    >(
      authRequest.post("/restore-password", payload, { headers: { "Content-Type": "text/plain" } })
    );
    if (err || !res) {
      return { data: null, error: err?.response?.data.mensaje || err?.message };
    }

    return { data: res.data, error: null };
  }

  static async restorePassword(code: string, payload: RecoverPasswordRequest) {
    const [res, err] = await asyncWrapper<
      AxiosResponse<RequestRecoverPasswordResponse>,
      AxiosError<RequestRecoverPasswordError>
    >(authRequest.put(`/restore-password/${code}`, payload));
    if (err || !res) {
      return { data: null, error: err?.response?.data.mensaje || err?.message };
    }

    return { data: res.data, error: null };
  }
}

export default AuthController;
