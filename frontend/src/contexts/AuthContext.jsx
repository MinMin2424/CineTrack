/*
 * Created by minmin_tranova on 19.02.2026
 * Authentication Context Provider for managing user authentication state.
 */

import React, {createContext, useState, useContext, useEffect, useCallback, useRef} from "react";
import { loginUser, registerUser, logoutUser, refreshToken as callRefreshToken } from "../api/AuthApi";
import { getUserProfile } from "../api/UserApi";

const AuthContext = createContext(null);

const REFRESH_BEFORE_EXPIRY_MS = 30 * 1000;

/**
 * AuthProvider Component
 */
export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [userProfile, setUserProfile] = useState(null);
    const [token, setToken] = useState(() => localStorage.getItem("token"));
    const [loading, setLoading] = useState(true);
    const refreshTimerRef = useRef(null);

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

    const saveTokens = useCallback((accessToken, refreshToken, expiresAt) => {
        localStorage.setItem('token', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        localStorage.setItem('expiresAt', String(expiresAt));
    }, []);

    const clearTokens = useCallback(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('expiresAt');
    }, []);

    const doRefresh = useCallback(async () => {
        const storedRefreshToken = localStorage.getItem('refreshToken');
        if (!storedRefreshToken) {
            clearTokens();
            return;
        }
        try {
            const data = await callRefreshToken(storedRefreshToken);
            saveTokens(data.accessToken, data.refreshToken, data.expiresAt);
            setToken(data.accessToken);
            return data;
        } catch (error) {
            clearTokens();
            setToken(null);
            setUser(null);
            setUserProfile(null);
            window.location.href="/auth/login";
        }
    }, [saveTokens, clearTokens])

    const scheduleRefresh = useCallback((expiresAt) => {
        if (refreshTimerRef.current) {
            clearTimeout(refreshTimerRef.current);
        }
        const now = Date.now();
        const timeUntilRefresh = expiresAt - now - REFRESH_BEFORE_EXPIRY_MS;
        if (timeUntilRefresh <= 0) {
            doRefresh().then(data => {
                if (data) scheduleRefresh(data.expiresAt);
            });
            return;
        }
        refreshTimerRef.current = setTimeout(async () => {
            const data = await doRefresh();
            if (data) scheduleRefresh(data.expiresAt);
        }, timeUntilRefresh);
    }, [doRefresh]);

    /**
     * Fetches detailed user profile from the API.
     */
    const fetchUserProfile = useCallback(async (accessToken) => {
        const currentToken = accessToken || localStorage.getItem('token');
        if (!currentToken) return null;
        try {
            const profileData = await getUserProfile();
            const decoded = decodeToken(accessToken);
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
        // eslint-disable-next-line
    }, []);

    /**
     * Initial authentication check on component mount.
     */
    useEffect(() => {
        const initAuth = async () => {
            const storedToken = localStorage.getItem('token');
            const storedRefreshToken = localStorage.getItem('refreshToken');
            const storedExpiresAt = localStorage.getItem('expiresAt');
            if (storedToken && storedExpiresAt) {
                const expiresAt = parseInt(storedExpiresAt, 10);
                if (expiresAt > Date.now()) {
                    setToken(storedToken);
                    await fetchUserProfile(storedToken);
                    scheduleRefresh(expiresAt);
                } else if (storedRefreshToken) {
                    const data = await doRefresh();
                    if (data) {
                        await fetchUserProfile(data.accessToken);
                        scheduleRefresh(data.expiresAt);
                    }
                } else {
                    clearTokens();
                }
            }
            setLoading(false);
        };
        initAuth();
        return () => {
            if (refreshTimerRef.current) {
                clearTimeout(refreshTimerRef.current);
            }
        };
        // eslint-disable-next-line
    }, []);

    /**
     * Login function
     */
    const login = useCallback(async (email, password) => {
        const data = await loginUser({ email, password });
        console.log('Login response', data);
        saveTokens(data.accessToken, data.refreshToken, data.expiresAt);
        setToken(data.accessToken);
        setUser(decodeToken(data.accessToken));
        scheduleRefresh(data.expiresAt)
        await fetchUserProfile(data.accessToken);
        return data;
    }, [fetchUserProfile, saveTokens, scheduleRefresh]);

    /**
     * Register function
     */
    const register = useCallback(async (registerData) => {
        const data = await registerUser(registerData);
        saveTokens(data.accessToken, data.refreshToken, data.expiresAt);
        setToken(data.accessToken);
        setUser(decodeToken(data.accessToken));
        scheduleRefresh(data.expiresAt)
        await fetchUserProfile(data.accessToken);
        return data;
    }, [fetchUserProfile, saveTokens, scheduleRefresh]);

    /**
     * Logout function
     */
    const logout = useCallback(async () => {
        if (refreshTimerRef.current) {
            clearTimeout(refreshTimerRef.current);
        }
        try {
            await logoutUser();
        } finally {
            clearTokens();
            setToken(null);
            setUser(null);
            setUserProfile(null);
        }
    }, [clearTokens]);

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