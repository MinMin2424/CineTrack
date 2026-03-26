/*
 * Created by minmin_tranova on 24.03.2026
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /statistics/status-overview
 * @returns {Promise<any>}
 */
export const getStatusOverview = async () => {
    const response = await axiosConfig.get("/statistics/status-overview");
    return response.data;
}

/**
 * GET /statistics/summary
 * @returns {Promise<any>}
 */
export const getSummary = async () => {
    const response = await axiosConfig.get("/statistics/summary");
    return response.data;
}

/**
 * GET /statistics/top-genres
 * @param limit
 * @returns {Promise<any>}
 */
export const getTopGenres = async (limit = 15) => {
    const response = await axiosConfig.get("/statistics/top-genres", {params : {limit}});
    return response.data;
}

/**
 * GET /statistics/top-countries
 * @param limit
 * @returns {Promise<any>}
 */
export const getTopCountries = async (limit = 5) => {
    const response = await axiosConfig.get("/statistics/top-countries", {params: {limit}});
    return response.data;
}

/**
 * GET /statistics/monthly-active
 * @returns {Promise<any>}
 */
export const getMonthlyActive = async (year, month) => {
    const response = await axiosConfig.get("/statistics/monthly-active", {params : {year, month}});
    return response.data;
}

/**
 * GET /statistics/other
 * @returns {Promise<any>}
 */
export const getOtherStats = async () => {
    const response = await axiosConfig.get("/statistics/other");
    return response.data;
}

/**
 * GET /statistics/fun-facts
 * @returns {Promise<any>}
 */
export const getFunFacts = async () => {
    const response = await axiosConfig.get("/statistics/fun-facts");
    return response.data;
}

/**
 * GET /statistics/top-rated
 * @returns {Promise<any>}
 */
export const getTopRated = async () => {
    const response = await axiosConfig.get("/statistics/top-rated");
    return response.data;
}