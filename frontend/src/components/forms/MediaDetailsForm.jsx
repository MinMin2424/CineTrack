/*
 * Created by minmin_tranova on 26.02.2026
 */

import React from "react";
import { IoIosArrowDown } from "react-icons/io";
import "../../styles/components/forms/AddMediaFormStyle.css"
import {useMediaFormValidation} from "../../hooks/UseMediaFormValidation";
import InputField from "./InputField";

const STATUS_OPTIONS = [
    { value: "plan to watch", label: "Plan to Watch" },
    { value: "watching", label: "Watching" },
    { value: "completed", label: "Completed" },
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
    const {fieldErrors} = useMediaFormValidation(formData, ["rating", "dates"]);
    const hasErrors = Object.keys(fieldErrors).length > 0;
    const handleSubmit = () => {
        if (hasErrors) return;
        onSubmit();
    };
    const isCompleted = formData.status === "completed";

    return (
        <>
            <div className="modal-body">

                {/* TITLE */}
                <InputField
                    label="Title" required readOnly
                    name="title"
                    value={formData.title}
                    onChange={onChange}
                />

                {/* START / END DATE */}
                <div className="modal-row">
                    <InputField
                        label={<>Start watching date {isCompleted && <span className="required-star"> *</span>}</>}
                        type="date"
                        name="watchStartDate"
                        value={formData.watchStartDate || ""}
                        onChange={onChange}
                        error={fieldErrors.watchStartDate}
                    />
                    <InputField
                        label={<>End watching date {isCompleted && <span className="required-star"> *</span>}</>}
                        type="date"
                        name="watchEndDate"
                        value={formData.watchEndDate || ""}
                        onChange={onChange}
                        error={fieldErrors.watchEndDate}
                    />
                </div>

                {/* STATUS + RATING */}
                <div className="modal-row">
                    <div className="modal-field">
                        <label className="modal-label">Watch status <span className="required-star"> *</span></label>
                        <div className="modal-select-wrapper">
                            <InputField
                                type="select"
                                name="status"
                                onChange={onChange}
                            >
                                {STATUS_OPTIONS.map(status => (
                                    <option key={status.value} value={status.value}>
                                        {status.label}
                                    </option>
                                ))}
                            </InputField>
                            <IoIosArrowDown className="modal-select-icon" />
                        </div>
                    </div>
                    <InputField
                        label="Rating (out of 10)"
                        type="number"
                        name="rating"
                        placeholder="8.5"
                        min="0"
                        max="10"
                        step="0.1"
                        value={formData.rating}
                        onChange={onChange}
                        error={fieldErrors.rating}
                    />
                </div>

                {/* PERSONAL NOTES */}
                <InputField
                    label="Personal notes"
                    type="textarea"
                    name="notes"
                    rows={4}
                    value={formData.notes}
                    onChange={onChange}
                    placeholder="Add your thoughts, reviews, reminders, ..."
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
                    onClick={handleSubmit}
                    disabled={loading || !formData.title || !formData.status}
                >
                    {loading ? "Adding..." : submitLabel}
                </button>
            </div>
        </>
    );
};

export default MediaDetailsForm;