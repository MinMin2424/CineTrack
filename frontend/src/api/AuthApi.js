/*
 * Created by minmin_tranova on 18.02.2026
 */

import axiosConfig from "./AxiosConfig";

export const registerUser = async (registerData) => {
    const response = await axiosConfig.post('/auth/register', registerData);
    return response.data;
};

export const loginUser = async (loginData) => {
    const response = await axiosConfig.post('/auth/login', loginData);
    return response.data;
};

export const logoutUser = async () => {
     await axiosConfig.post('/auth/logout');
};

export const refreshToken = async () => {
    const response = await axiosConfig.post('/auth/refresh');
    return response.data;
};