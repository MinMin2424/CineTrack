/*
 * Created by minmin_tranova on 25.02.2026
 */

import React, { useState } from "react";
import { createMovie } from "../../api/MediaApi";
import {IoClose} from "react-icons/io5";
import MediaDetailsForm from "./MediaDetailsForm"
import "../../styles/components/addMediaForms/AddMediaFormStyle.css"

const AddMovieForm = ({ omdbData, onSuccess, onBack, onClose}) => {

    const [formData, setFormData] = useState({
        imdbID: omdbData?.imdbID || "",
        title: omdbData?.Title || "",
        year: omdbData?.Year || "",
        runtime: omdbData?.Runtime || "",
        genre: omdbData?.Genre || "",
        language: omdbData?.Language || "",
        country: omdbData?.Country || "",
        poster: omdbData?.Poster || "",
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
            await createMovie({
                ...formData,
                rating: formData.rating || "0",
                watchStartDate: formData.watchStartDate || null,
                watchEndDate: formData.watchEndDate || null,
            });
            onSuccess();
        } catch (error) {
            setError(error.response?.data?.error || "Failed to add movie. Please try again.")
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
                  submitLabel="Add Movie"
              />
          </div>
      </div>
    );
};

export default AddMovieForm;