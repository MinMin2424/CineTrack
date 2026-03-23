/*
 * Created by minmin_tranova on 13.03.2026
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /series/${seriesId}
 * @param seriesId
 * @returns {Promise<any>}
 */
export const getSeries = async (seriesId) => {
    const response = await axiosConfig.get(`/series/${seriesId}`);
    return response.data;
}

/**
 * PUT /series/${seriesId}
 * @param seriesId
 * @param data
 * @returns {Promise<any>}
 */
export const editSeries = async (seriesId, data) => {
    const response = await axiosConfig.put(`/series/${seriesId}`, data);
    return response.data;
}

const STATUS_MAP = {
    WATCHING: "watching",
    COMPLETED: "completed",
    DROPPED: "dropped",
    PLAN_TO_WATCH: "plan to watch"
}

/**
 * PUT /series/${seriesId}/status
 * @param seriesId
 * @param status
 * @returns {Promise<any>}
 */
export const changeSeriesStatus = async (seriesId, status) => {
    const response = await axiosConfig.put(`/series/${seriesId}/status`, {status: STATUS_MAP[status] ?? status.toLowerCase()});
    return response.data;
}

/**
 * DELETE /series/${seriesId}
 * @param seriesId
 * @returns {Promise<any>}
 */
export const deleteSeries = async (seriesId) => {
    const response = await axiosConfig.delete(`/series/${seriesId}`);
    return response.data;
}