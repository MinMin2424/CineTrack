/*
 * Created by minmin_tranova on 15.03.2026
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /series/${seriesId}/episodes/${episodeNumber}
 * @param seriesId
 * @param episodeNumber
 * @returns {Promise<any>}
 */
export const getEpisode = async (seriesId, episodeNumber) => {
    const response = await axiosConfig.get(`/series/${seriesId}/episodes/${episodeNumber}`);
    return response.data;
}

const STATUS_MAP = {
    WATCHING: "watching",
    PAUSED: "paused",
    COMPLETED: "completed",
    DROPPED: "dropped",
    PLAN_TO_WATCH: "plan to watch",
    NONE: "none"
}

/**
 * PUT /series/${seriesId}/episodes/{episodeNumber}/status
 * @param seriesId
 * @param episodeNumber
 * @param status
 * @returns {Promise<any>}
 */
export const changeEpisodeStatus = async (seriesId, episodeNumber, status) => {
    const response = await axiosConfig.put(`/series/${seriesId}/episodes/${episodeNumber}/status`, {status: STATUS_MAP[status] ?? status.toLowerCase()});
    return response.data;
}

/**
 * PUT /series/${seriesId}/episodes/${episodeNumber}
 * @param seriesId
 * @param episodeNumber
 * @param data
 * @returns {Promise<any>}
 */
export const editEpisode = async (seriesId, episodeNumber, data) => {
    const response = await axiosConfig.put(`/series/${seriesId}/episodes/${episodeNumber}`, data);
    return response.data;
}