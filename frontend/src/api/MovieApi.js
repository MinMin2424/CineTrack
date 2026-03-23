/*
 * Created by minmin_tranova on 10.03.2026
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /movies/${movieId}
 * @param movieId
 * @returns {Promise<any>}
 */
export const getMovie = async (movieId) => {
    const response = await axiosConfig.get(`/movies/${movieId}`);
    return response.data;
}

/**
 * PUT /movies/${movieId}
 * @param movieId
 * @param data
 * @returns {Promise<any>}
 */
export const editMovie = async (movieId, data) => {
    const response = await axiosConfig.put(`/movies/${movieId}`, data);
    return response.data;
}

const STATUS_MAP = {
    WATCHING: "watching",
    COMPLETED: "completed",
    DROPPED: "dropped",
    PLAN_TO_WATCH: "plan to watch"
}

/**
 * PUT /movies/${movieId}/status
 * @param movieId
 * @param status
 * @returns {Promise<any>}
 */
export const changeMovieStatus = async (movieId, status) => {
    const response = await axiosConfig.put(`/movies/${movieId}/status`, {status: STATUS_MAP[status] ?? status.toLowerCase()});
    return response.data;
}

/**
 * DELETE /movies/${movieId}
 * @param movieId
 * @returns {Promise<any>}
 */
export const deleteMovie = async (movieId) => {
    const response = await axiosConfig.delete(`/movies/${movieId}`);
    return response.data;
}