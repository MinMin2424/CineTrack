/*
 * Created by minmin_tranova on 20.02.2026
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