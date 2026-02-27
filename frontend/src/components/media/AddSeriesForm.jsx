/*
 * Created by minmin_tranova on 25.02.2026
 */

import React, { useState } from "react";
import { createSeries } from "../../api/MediaApi";
import {IoClose} from "react-icons/io5";
import MediaDetailsForm from "./MediaDetailsForm"
import "../../styles/components/addMediaForms/AddMediaFormStyle.css"

const AddSeriesForm = ({ omdbData, onSuccess, onBack, onClose }) => {

    const [formData, setFormData] = useState({
        imdbID: omdbData?.imdbID || "",
        title: omdbData?.Title || "",
        genre: omdbData?.Genre || "",
        language: omdbData?.Language || "",
        country: omdbData?.Country || "",
        poster: omdbData?.Poster || "",
        season: omdbData?.selectedSeason?.toString() || "1",
        status: "plan to watch",
        rating: "",
        notes: "",
        watchStartDate: null,
        watchEndDate: null,
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async () => {
        setLoading(true);
        setError(null);
        try {
            await createSeries({
                ...formData,
                rating: formData.rating || "0",
                watchStartDate: formData.watchStartDate || null,
                watchEndDate: formData.watchEndDate || null,
            });
            onSuccess();
        } catch (error) {
            setError(error.response?.data?.error || "Failed to add TV series. Please try again.")
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-container" onClick={e => e.stopPropagation()}>
                <div className="modal-header">
                    <h2 className="modal-title">Add New Media</h2>
                    <button className="modal-close-btn" onClick={onClose}>
                        <IoClose />
                    </button>
                </div>
                <MediaDetailsForm
                    formData={formData}
                    onChange={handleChange}
                    onSubmit={handleSubmit}
                    onBack={onBack}
                    onClose={onClose}
                    loading={loading}
                    error={error}
                    submitLabel="Add TV Series"
                />
            </div>
        </div>
    );
};

export default AddSeriesForm;