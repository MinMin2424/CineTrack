/*
 * Created by minmin_tranova on 18.02.2026
 * Axios config for API communication with automatic token handling and response processing.
 */

import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/api';

const axiosConfig = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

/**
 * Request interceptor
 * Adds authentication token to the header if it exists.
 */
axiosConfig.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

/**
 * Response interceptor
 * Handles new tokens and error states.
 */
axiosConfig.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        const isAuthRequest = error.config?.url?.includes('/auth/');
        if (isAuthRequest) {
            return Promise.reject(error);
        }

        if (error.response?.status !== 401 || originalRequest._retry) {
            return Promise.reject(error);
        }
        originalRequest._retry = true;

        const storedRefreshToken = localStorage.getItem('refreshToken');
        if (!storedRefreshToken) {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('expiresAt');
            window.location.href = '/auth/login';
            return Promise.reject(error);
        }

        try {
            const refreshResponse = await axios.post(`${API_BASE_URL}/auth/refresh`, {
                refreshToken: storedRefreshToken,
            });
            const { accessToken, refreshToken: newRefreshToken, expiresAt } = refreshResponse.data;
            localStorage.setItem('token', accessToken);
            localStorage.setItem('refreshToken', newRefreshToken);
            localStorage.setItem('expiresAt', String(expiresAt));
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
            return axiosConfig(originalRequest);
        } catch (error) {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('expiresAt');
            window.location.href = '/auth/login';
            return Promise.reject(error);
        }
    }
);

export default axiosConfig;