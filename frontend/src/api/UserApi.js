/*
 * Created by minmin_tranova on 25.02.2026
 */

import axiosConfig from "./AxiosConfig";

export const getUserProfile = async () => {
    try {
        const response = await axiosConfig.get("/users/me/profile");
        return response.data;
    } catch (error) {
        console.error("Failed to get user profile", error);
    }
};