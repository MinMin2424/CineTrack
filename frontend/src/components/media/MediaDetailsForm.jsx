/*
 * Created by minmin_tranova on 26.02.2026
 */

import React from "react";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/components/addMediaForms/AddMediaFormStyle.css"

const STATUS_OPTIONS = [
    { value: "plan to watch", label: "Plan to Watch" },
    { value: "watching", label: "Watching" },
    { value: "completed", label: "Completed" },
    { value: "paused", label: "Paused" },
    { value: "dropped", label: "Dropped" },
];

const MediaDetailsForm = ({
   formData,
   onChange,
   onSubmit,
   onBack,
   onClose,
   loading,
   error,
   submitLabel
}) => {
    return (
        <>
            <div className="modal-body">

                {/* TITLE */}
                <label className="modal-label">Title *</label>
                <input
                    type="text"
                    name="title"
                    className="modal-input"
                    value={formData.title}
                    onChange={onChange}
                    readOnly
                />

                {/* START / END DATE */}
                <div className="modal-row">
                    <div className="modal-field">
                        <label className="modal-label">Start watching date</label>
                        <input
                            type="date"
                            name="watchStartDate"
                            className="modal-input"
                            value={formData.watchStartDate || ""}
                            onChange={onChange}
                        />
                    </div>
                    <div className="modal-field">
                        <label className="modal-label">End watching date</label>
                        <input
                            type="date"
                            name="watchEndDate"
                            className="modal-input"
                            value={formData.watchEndDate || ""}
                            onChange={onChange}
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
                                onChange={onChange}
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
                            onChange={onChange}
                        />
                    </div>
                </div>

                {/* PERSONAL NOTES */}
                <label className="modal-label">Personal notes</label>
                <textarea
                    name="notes"
                    className="modal-input modal-textarea"
                    placeholder="Add your thoughts, reviews, reminders, ..."
                    value={formData.notes}
                    onChange={onChange}
                    rows={4}
                />
                <div className="modal-error-wrapper">
                    {error && <p className="modal-error">{error}</p>}
                </div>
            </div>

            <div className="modal-footer">
                <button className="modal-cancel-btn" onClick={onBack}>
                    Cancel
                </button>
                <button
                    className="modal-submit-btn"
                    onClick={onSubmit}
                    disabled={loading || !formData.title || !formData.status}
                >
                    {loading ? "Adding..." : submitLabel}
                </button>
            </div>
        </>
    );
};

export default MediaDetailsForm;