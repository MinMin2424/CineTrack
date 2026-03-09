/*
 * Created by minmin_tranova on 08.03.2026
 */

import React, { useState, useEffect } from "react";
import {IoClose} from "react-icons/io5";
import {IoIosArrowDown} from "react-icons/io";
import { createSeriesManually, getAllGenres, getAllLanguages, getAllCountries } from "../../api/MediaApi";
import MultiSelectDropdown from "./MultiSelectDropdown";
import "../../styles/components/forms/AddMediaFormStyle.css"
import PosterUpload from "./PosterUpload";

const STATUS_OPTIONS = [
    { value: "plan to watch", label: "Plan to Watch" },
    { value: "watching", label: "Watching" },
    { value: "completed", label: "Completed" },
    { value: "paused", label: "Paused" },
    { value: "dropped", label: "Dropped" },
];

const AddSeriesManualForm = ({
    onSuccess,
    onBack,
    onClose,
    onSwitchToMovie,
}) => {
    const [formData, setFormData] = useState({
        title: "",
        releaseYear: "",
        season: "1",
        episodeCount: "",
        posterUrl: "",
        watchStartDate: "",
        watchEndDate: "",
        status: "plan to watch",
        rating: "",
        notes: "",
        genre: [],
        language: [],
        country: []
    });
    const [genreOptions, setGenresOptions] = useState([]);
    const [languageOptions, setLanguageOptions] = useState([]);
    const [countryOptions, setCountryOptions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [fieldErrors, setFieldErrors] = useState({});

    useEffect(() => {
        Promise.all([getAllGenres(), getAllLanguages(), getAllCountries()])
            .then(([genres, languages, countries]) => {
                setGenresOptions(genres.map(g => ({ id: g.id, label: g.type })));
                setLanguageOptions(languages.map(l => ({ id: l.id, label: l.lang })));
                setCountryOptions(countries.map(c => ({ id: c.id, label: c.countryName })));
            })
            .catch(err => console.error("Failed to load metadata", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        setFieldErrors(prev => ({ ...prev, [name]: null }));
    };

    const handleMultiToggle = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: prev[field].includes(value)
                ? prev[field].filter(v => v !== value)
                : [...prev[field], value],
        }));
        setFieldErrors(prev => ({ ...prev, [field]: null }));
    };

    const validate = () => {
        const e = {};
        if (!formData.title.trim()) e.title = "Title is required";
        if (!formData.releaseYear) e.releaseYear = "Released year is required";
        if (!formData.season) e.season = "Season is required";
        if (!formData.episodeCount) e.episodeCount = "Episode count is required";
        if (!formData.posterUrl.trim()) e.posterUrl = "Poster is required";
        if (formData.genre.length === 0) e.genre = "At least one genre is required";
        if (formData.language.length === 0) e.language = "At least one language is required";
        if (formData.country.length === 0) e.country = "At least one country is required";
        return e;
    };

    const handleSubmit = async () => {
        const errors = validate();
        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }
        setLoading(true);
        setError(null);
        try {
            await createSeriesManually({
                title: formData.title,
                releaseYear: parseInt(formData.releaseYear, 10),
                season: parseInt(formData.season, 10),
                episodeCount: parseInt(formData.episodeCount, 10),
                posterUrl: formData.posterUrl,
                watchStartDate: formData.watchStartDate || null,
                watchEndDate: formData.watchEndDate || null,
                status: formData.status,
                rating: parseFloat(formData.rating) || 0,
                notes: formData.notes || "",
                genre: formData.genre,
                language: formData.language,
                country: formData.country,
            });
            onSuccess();
        } catch (error) {
            setError(error.response?.data?.error || "Failed to add TV series. Please try again.")
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-container" onClick={e => e.stopPropagation()}>

                <div className="modal-header">
                    <h2 className="modal-title">Add New Media</h2>
                    <button className="modal-close-btn" onClick={onClose}>
                        <IoClose />
                    </button>
                </div>

                <div className="modal-body">

                    {/* TYPE TOGGLE */}
                    <label className="modal-label">Media Type</label>
                    <div className="modal-type-toggle">
                        <button className="modal-type-btn" onClick={onSwitchToMovie}>Movie</button>
                        <button className="modal-type-btn active">Series</button>
                    </div>

                    {/* TITLE */}
                    <label className="modal-label">Title *</label>
                    <input
                        type="text"
                        name="title"
                        autoFocus
                        className={`modal-input ${fieldErrors.title ? "input-error" : ""}`}
                        placeholder="Enter movie title"
                        value={formData.title}
                        onChange={handleChange}
                    />
                    {fieldErrors.title && <p className="manual-field-error">{fieldErrors.title}</p>}

                    {/* RELEASED YEAR + SEASON + EPISODES */}
                    <div className="modal-row">
                        <div className="modal-field">
                            <label className="modal-label">Released year *</label>
                            <input
                                type="number"
                                name="releaseYear"
                                className={`modal-input ${fieldErrors.releaseYear ? "input-error" : ""}`}
                                placeholder="2026"
                                value={formData.releaseYear}
                                onChange={handleChange}
                            />
                            {fieldErrors.releaseYear && <p className="manual-field-error">{fieldErrors.releaseYear}</p>}
                        </div>
                        <div className="modal-field">
                            <label className="modal-label">Season *</label>
                            <input
                                type="number"
                                name="season"
                                min="1"
                                className={`modal-input ${fieldErrors.season ? "input-error" : ""}`}
                                placeholder="1"
                                value={formData.season}
                                onChange={handleChange}
                            />
                            {fieldErrors.season && <p className="manual-field-error">{fieldErrors.season}</p>}
                        </div>
                        <div className="modal-field">
                            <label className="modal-label">Episode count *</label>
                            <input
                                type="number"
                                name="episodeCount"
                                min="1"
                                className={`modal-input ${fieldErrors.episodeCount ? "input-error" : ""}`}
                                placeholder="24"
                                value={formData.episodeCount}
                                onChange={handleChange}
                            />
                            {fieldErrors.episodeCount && <p className="manual-field-error">{fieldErrors.episodeCount}</p>}
                        </div>
                    </div>

                    {/* DATES */}
                    <div className="modal-row">
                        <div className="modal-field">
                            <label className="modal-label">Start watching date</label>
                            <input
                                type="date"
                                name="watchStartDate"
                                className={`modal-input ${fieldErrors.watchStartDate ? "input-error" : ""}`}
                                value={formData.watchStartDate}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="modal-field">
                            <label className="modal-label">End watching date</label>
                            <input
                                type="date"
                                name="watchEndDate"
                                className={`modal-input ${fieldErrors.watchEndDate ? "input-error" : ""}`}
                                value={formData.watchEndDate}
                                onChange={handleChange}
                            />
                        </div>
                    </div>

                    {/* STATUS + RATING */}
                    <div className="modal-row">
                        <div className="modal-field">
                            <label className="modal-label">Watch status *</label>
                            <div className="modal-select-wrapper">
                                <select
                                    name="status"
                                    className="modal-input modal-select"
                                    value={formData.status}
                                    onChange={handleChange}
                                >
                                    {STATUS_OPTIONS.map(status => (
                                        <option key={status.value} value={status.value}>
                                            {status.label}
                                        </option>
                                    ))}
                                </select>
                                <IoIosArrowDown className="modal-select-icon" />
                            </div>
                        </div>
                        <div className="modal-field">
                            <label className="modal-label">Rating (out of 10)</label>
                            <input
                                type="number"
                                name="rating"
                                className="modal-input"
                                placeholder="8.5"
                                min="0"
                                max="10"
                                step="0.1"
                                value={formData.rating}
                                onChange={handleChange}
                            />
                        </div>
                    </div>

                    {/* NOTES */}
                    <label className="modal-label">Personal notes</label>
                    <textarea
                        name="notes"
                        className="modal-input modal-textarea"
                        placeholder="Add your thoughts, reviews, reminders, ..."
                        value={formData.notes}
                        onChange={handleChange}
                        rows={4}
                    />

                    {/* GENRES */}
                    <MultiSelectDropdown
                        label="Genres *"
                        placeholder="Choose TV series genres"
                        options={genreOptions}
                        selected={formData.genre}
                        onToggle={val => handleMultiToggle("genre", val)}
                        error={fieldErrors.genre}
                    />

                    {/* LANGUAGES */}
                    <MultiSelectDropdown
                        label="Languages *"
                        placeholder="Choose TV series languages"
                        options={languageOptions}
                        selected={formData.language}
                        onToggle={val => handleMultiToggle("language", val)}
                        error={fieldErrors.language}
                    />

                    {/* COUNTRIES */}
                    <MultiSelectDropdown
                        label="Countries *"
                        placeholder="Choose TV series countries"
                        options={countryOptions}
                        selected={formData.country}
                        onToggle={val => handleMultiToggle("country", val)}
                        error={fieldErrors.country}
                    />

                    {/* POSTER */}
                    <PosterUpload
                        value={formData.posterUrl}
                        onChange={(url) => {
                            setFormData(prev => ({...prev, posterUrl: url}));
                            setFieldErrors(prev => ({...prev, posterUrl: null}));
                        }}
                        error={fieldErrors.posterUrl}
                    />

                    {error && (
                        <div className="modal-error-wrapper">
                            {error && <p className="modal-error">{error}</p>}
                        </div>
                    )}
                </div>

                <div className="modal-footer">
                    <button className="modal-cancel-btn" onClick={onBack}>
                        Cancel
                    </button>
                    <button
                        className="modal-submit-btn"
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        {loading ? "Adding..." : "Add TV series"}
                    </button>
                </div>

            </div>
        </div>
    );
};

export default AddSeriesManualForm;