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