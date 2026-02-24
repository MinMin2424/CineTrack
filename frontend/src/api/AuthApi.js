/*
 * Created by minmin_tranova on 18.02.2026
 */

import axiosConfig from "./AxiosConfig";

/**
 * POST /auth/register
 * @param registerData
 * @returns {Promise<any>}
 */
export const registerUser = async (registerData) => {
    const response = await axiosConfig.post('/auth/register', registerData);
    return response.data;
};

/**
 * POST /auth/login
 * @param loginData
 * @returns {Promise<any>}
 */
export const loginUser = async (loginData) => {
    const response = await axiosConfig.post('/auth/login', loginData);
    return response.data;
};

/**
 * POST /auth/logout
 * @returns {Promise<void>}
 */
export const logoutUser = async () => {
     await axiosConfig.post('/auth/logout');
};

/**
 * POST /auth/refresh
 * @returns {Promise<any>}
 */
export const refreshToken = async () => {
    const response = await axiosConfig.post('/auth/refresh');
    return response.data;
};