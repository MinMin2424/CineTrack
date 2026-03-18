/*
 * Created by minmin_tranova on 20.02.2026
 * API functions for media management and filtering.
 */

import axiosConfig from "./AxiosConfig";

/**
 * GET /media/overview
 * @param sortBy
 * @param filters
 * @returns {Promise<any>}
 */
export const getMediaOverview = async (sortBy = 'CREATED_AT_DESC', filters = {}) => {
    const params = new URLSearchParams();
    params.append('sortBy', sortBy);

    if (filters.types?.length) {
        filters.types.forEach(t => params.append("types", t));
    }
    if (filters.statuses?.length) {
        filters.statuses.forEach(s => params.append("statuses", s));
    }
    if (filters.genreIds?.length) {
        filters.genreIds?.forEach(id => params.append("genreIds", id));
    }
    if (filters.releaseYears?.length) {
        filters.releaseYears.forEach(y => params.append("releaseYears", y));
    }
    if (filters.countryIds?.length) {
        filters.countryIds.forEach(id => params.append("countryIds", id));
    }
    const response = await axiosConfig.get(`/media/overview?${params.toString()}`);
    return response.data;
}

/**
 * GET /media/filter-options
 * @returns {Promise<any>}
 */
export const getFilterOptions = async () => {
    const response = await axiosConfig.get(`/media/filter-options`);
    return response.data;
}

/**
 * GET /media/autocomplete?query=...&limit=...
 * @param query
 * @param limit
 * @returns {Promise<any>}
 */
export const autocompleteTitles = async (query, limit = 10) => {
    const response = await axiosConfig.get('media/autocomplete', {
        params: { query, limit }
    });
    return response.data;
}

/**
 * POST /media/search
 * @param title
 * @param type
 * @returns {Promise<any>}
 */
export const searchMedia = async (title, type) => {
    const response = await axiosConfig.post('/media/search', {title, type});
    return response.data;
}

/**
 * GET /media/discover
 * @param query
 * @param limit
 * @returns {Promise<any>}
 */
export const discoverMedia = async (query, limit = "10") => {
    const response = await axiosConfig.get(`/media/discover?query=${encodeURIComponent(query)}&limit=${limit}`);
    return response.data;}

/**
 * POST /media/movie
 * @param data
 * @returns {Promise<any>}
 */
export const createMovie = async (data) => {
    const response = await axiosConfig.post('/media/movie', data);
    return response.data;
}

/**
 * POST /media/series
 * @param data
 * @returns {Promise<any>}
 */
export const createSeries = async (data) => {
    const response = await axiosConfig.post('/media/series', data);
    return response.data;
}

/**
 * POST /media/manual/movie
 * @param data
 * @returns {Promise<any>}
 */
export const createMovieManually = async (data) => {
    const response = await axiosConfig.post('/media/manual/movie', data);
    return response.data;
}

/**
 * POST /media/manual/series
 * @param data
 * @returns {Promise<any>}
 */
export const createSeriesManually = async (data) => {
    const response = await axiosConfig.post('/media/manual/series', data);
    return response.data;
}

/**
 * GET /media/metadata/genres
 * @returns {Promise<any>}
 */
export const getAllGenres = async () => {
    const response = await axiosConfig.get('/media/metadata/genres');
    return response.data;
}

/**
 * GET /media/metadata/languages
 * @returns {Promise<any>}
 */
export const getAllLanguages = async () => {
    const response = await axiosConfig.get('/media/metadata/languages');
    return response.data;
}

/**
 * GET /media/metadata/countries
 * @returns {Promise<any>}
 */
export const getAllCountries = async () => {
    const response = await axiosConfig.get('/media/metadata/countries');
    return response.data;
}