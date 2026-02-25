/*
 * Created by minmin_tranova on 19.02.2026
 * Authentication Context Provider for managing user authentication state.
 */

import React, { createContext, useState, useContext, useEffect, useCallback } from "react";
import { loginUser, registerUser, logoutUser } from "../api/AuthApi";
import { getUserProfile } from "../api/UserApi";

const AuthContext = createContext(null);

/**
 * AuthProvider Component
 */
export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [userProfile, setUserProfile] = useState(null);
    const [token, setToken] = useState(() => localStorage.getItem("token"));
    const [loading, setLoading] = useState(true);

    /**
     * Decodes JWT token to extract user information.
     * @param jwtToken
     * @returns {any|null}
     */
    const decodeToken = (jwtToken) => {
        try {
            const payload = jwtToken.split('.')[1]
            return JSON.parse(atob(payload));
        } catch {
            return null;
        }
    };

    /**
     * Fetches detailed user profile from the API.
     */
    const fetchUserProfile = useCallback(async () => {
        if (!token) return null;
        try {
            const profileData = await getUserProfile();
            const decoded = decodeToken(token);
            setUserProfile(profileData);
            setUser({
                ...decoded,
                ...profileData.profile,
                header: profileData.header,
            });
            return profileData;
        } catch (error) {
            console.error("Failed to get user profile", error);
            const decoded = decodeToken(token);
            setUser(decoded);
            return null;
        }
    }, [token]);

    /**
     * Initial authentication check on component mount.
     */
    useEffect(() => {
        const initAuth = async () => {
            if (token) {
                const decoded = decodeToken(token);
                if (decoded && decoded.exp * 1000 > Date.now()) {
                    await fetchUserProfile();
                } else {
                    localStorage.removeItem("token");
                    setToken(null);
                }
            }
            setLoading(false);
        };
        initAuth();
    }, [token, fetchUserProfile]);

    /**
     * Login function
     */
    const login = useCallback(async (email, password) => {
        const data = await loginUser({ email, password });
        localStorage.setItem("token", data.token);
        setToken(data.token);
        setUser(decodeToken(data.token));
        await fetchUserProfile();
        return data;
    }, [fetchUserProfile]);

    /**
     * Register function
     */
    const register = useCallback(async (registerData) => {
        const data = await registerUser(registerData);
        localStorage.setItem("token", data.token);
        setToken(data.token);
        setUser(decodeToken(data.token));
        await fetchUserProfile();
        return data;
    }, [fetchUserProfile]);

    /**
     * Logout function
     */
    const logout = useCallback(async () => {
        try {
            await logoutUser();
        } finally {
            localStorage.removeItem("token");
            setToken(null);
            setUser(null);
            setUserProfile(null);
        }
    }, []);

    const value = {
        user,
        userProfile,
        token,
        isAuthenticated: !!user,
        login,
        register,
        logout,
        loading,
        refreshProfile: fetchUserProfile,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Throws error if used outside AuthProvider.
 */
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within a AuthProvider');
    }
    return context;
};