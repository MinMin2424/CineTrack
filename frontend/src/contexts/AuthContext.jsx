/*
 * Created by minmin_tranova on 19.02.2026
 */

import React, { createContext, useState, useContext, useEffect, useCallback } from "react";
import { loginUser, registerUser, logoutUser } from "../api/AuthApi";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(() => localStorage.getItem("token"));
    const [loading, setLoading] = useState(true);

    const decodeToken = (jwtToken) => {
        try {
            const payload = jwtToken.split('.')[1]
            return JSON.parse(atob(payload));
        } catch {
            return null;
        }
    };

    useEffect(() => {
        if (token) {
            const decoded = decodeToken(token);
            if (decoded && decoded.exp * 1000 > Date.now()) {
                setUser(decoded);
            } else {
                localStorage.removeItem("token");
                setToken(null);
            }
        }
        setLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const login = useCallback(async (email, password) => {
        const data = await loginUser({ email, password });
        localStorage.setItem("token", data.token);
        setToken(data.token);
        setUser(decodeToken(data.token));
        return data;
    }, []);

    const register = useCallback(async (registerData) => {
        const data = await registerUser(registerData);
        localStorage.setItem("token", data.token);
        setToken(data.token);
        setUser(decodeToken(data.token));
        return data;
    }, []);

    const logout = useCallback(async () => {
        try {
            await logoutUser();
        } finally {
            localStorage.removeItem("token");
            setToken(null);
            setUser(null);
        }
    }, []);

    const value = {
        user,
        token,
        isAuthenticated: !!user,
        login,
        register,
        logout,
        loading,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within a AuthProvider');
    }
    return context;
};