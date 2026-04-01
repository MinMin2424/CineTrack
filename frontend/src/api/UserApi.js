/*
 * Created by minmin_tranova on 25.02.2026
 * API function for fetching user profile data.
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /users/me/profile
 * @returns {Promise<any>}
 */
export const getUserProfile = async () => {
    try {
        const response = await axiosConfig.get("/users/me/profile");
        return response.data;
    } catch (error) {
        console.error("Failed to get user profile", error);
    }
};

/**
 * PUT /users/me/profile
 * @param data
 * @returns {Promise<any>}
 */
export const editUserProfile = async (data) => {
    const response = await axiosConfig.put("/users/me/profile", data);
    return response.data;
};

/**
 * PUT /users/me/password
 * @param data
 * @returns {Promise<any>}
 */
export const changeUserPassword = async (data) => {
    await axiosConfig.put("/users/me/password", data);
};

/**
 * PUT /users/me/avatar
 * @param data
 * @returns {Promise<any>}
 */
export const changeUserAvatar = async (data) => {
    await axiosConfig.put("/users/me/avatar", data);
};

/**
 * DELETE /users/me
 * @returns {Promise<void>}
 */
export const deleteCurrentUser = async () => {
    await axiosConfig.delete("/users/me");
};