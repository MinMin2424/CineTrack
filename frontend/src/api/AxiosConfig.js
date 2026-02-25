/*
 * Created by minmin_tranova on 18.02.2026
 * Axios config for API communication with automatic token handling and response processing.
 */

import axios from "axios";

const API_BASE_URL = 'http://localhost:8080/api';

const axiosConfig = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
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
    (response) => {
        const newToken = response.headers['x-new-token'];
        if (newToken) {
            localStorage.setItem('token', newToken);
        }
        return response;
    },
     (error) => {
        const isLoginRequest = error.config?.url?.includes('/auth/login');
        if (isLoginRequest) {
            return Promise.reject(error);
        }
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/auth/login';
        }
        return Promise.reject(error);
    }
);

export default axiosConfig;