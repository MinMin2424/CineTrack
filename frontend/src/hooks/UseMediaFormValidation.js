/*
 * Created by minmin_tranova on 09.03.2026
 */

import { useState, useEffect } from "react";

const CURRENT_YEAR = new Date().getFullYear();

export const useMediaFormValidation = (formData, rules = []) => {
    const [fieldErrors, setFieldErrors] = useState({});

    useEffect(() => {
        const errors = {};

        if (rules.includes("rating")) {
            const rating = parseFloat(formData.rating);
            if (formData.rating !== "" && (isNaN(rating) || rating < 0 || rating > 10)) {
                errors.rating = "Rating must be between 0 and 10";
            }
        }

        if (rules.includes("dates")) {
            const start = formData.watchStartDate || null;
            const end = formData.watchEndDate || null;
            if (end && !start) {
                errors.watchStartDate = "Start date is required when end date is set";
            }
            if (start && end && new Date(end) < new Date(start)) {
                errors.watchEndDate = "End date cannot be before start date";
            }
            if (formData.status === "completed") {
                if (!start) errors.watchStartDate = "Start date is required when status is Completed";
                if (!end) errors.watchEndDate = "End date is required when status is Completed";
            }
        }

        if (rules.includes("releaseYear")) {
            const year = parseInt(formData.releaseYear);
            if (formData.releaseYear !== "" && (isNaN(year) || year < 1700 || year > CURRENT_YEAR)) {
                errors.releaseYear = "Year is invalid";
            }
        }

        if (rules.includes("runtime")) {
            const runtime = parseInt(formData.runtime);
            if (formData.runtime !== "" && (isNaN(runtime) || runtime < 0 || runtime > 500)) {
                errors.runtime = "Runtime is invalid";
            }
        }
        
        if (rules.includes("season")) {
            const season = parseInt(formData.season);
            if (formData.season !== "" && (isNaN(season) || season < 0 || season > 50)) {
                errors.season = "Season is invalid";
            }
        }
        
        if (rules.includes("episodeCount")) {
            const episodeCount = parseInt(formData.episodeCount);
            if (formData.episodeCount !== "" && (isNaN(episodeCount) || episodeCount < 0 || episodeCount > 100)) {
                errors.episodeCount = "Episode count is invalid";
            }
        }

        setFieldErrors(errors);
        // eslint-disable-next-line
    }, [formData.releaseYear, formData.runtime, formData.watchStartDate, formData.watchEndDate, formData.status, formData.season, formData.episodeCount, formData.rating]);

    return {
        fieldErrors,
    };
};