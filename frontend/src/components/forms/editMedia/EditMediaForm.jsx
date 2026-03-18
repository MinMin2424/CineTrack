/*
 * Created by minmin_tranova on 13.03.2026
 */

import React from 'react';
import {IoClose} from "react-icons/io5";
import { useMediaFormValidation } from "../../../hooks/UseMediaFormValidation";
import InputField from "../InputField";
import "../../../styles/components/forms/AddMediaFormStyle.css"

const EditMediaForm = ({
    formData,
    onChange,
    onSubmit,
    onClose,
    loading,
    error
}) => {
    const {fieldErrors} = useMediaFormValidation(formData, ["rating", "dates"]);
    const hasErrors = Object.keys(fieldErrors).length > 0;
    const handleSubmit = () => {
        if (hasErrors) return;
        onSubmit();
    };
    return (
        <div className="modal-overlay">
            <div className="modal-container" onClick={e => e.stopPropagation()}>

                {/* HEADER */}
                <div className="modal-header">
                    <h2 className="modal-title">Edit User Activity</h2>
                    <button className="modal-close-btn" onClick={onClose}>
                        <IoClose />
                    </button>
                </div>

                {/* BODY */}
                <div className="modal-body">
                    {/* START / END DATES */}
                    <div className="modal-row">
                        <InputField
                            label="Start watching date"
                            type="date"
                            name="watchStartDate"
                            className="watchStartDate"
                            value={formData.watchStartDate || ""}
                            onChange={onChange}
                            error={fieldErrors.watchStartDate}
                        />
                        <InputField
                            label="End watching date"
                            type="date"
                            name="watchEndDate"
                            className="watchEndDate"
                            value={formData.watchEndDate || ""}
                            onChange={onChange}
                            error={fieldErrors.watchEndDate}
                        />
                    </div>
                    {/* RATING */}
                    <InputField
                        label="Rating (out of 10)"
                        type="number"
                        name="rating"
                        placeholder="8.5"
                        min="0"
                        max="10"
                        step="0.1"
                        value={formData.rating ?? ""}
                        onChange={onChange}
                        error={fieldErrors.rating}
                    />
                    {/* NOTES */}
                    <InputField
                        label="Personal notes"
                        type="textarea"
                        name="notes"
                        rows={4}
                        value={formData.notes ?? ""}
                        onChange={onChange}
                        placeholder="Add your throughts, reviews, reminders, ..."
                    />
                    <div className="modal-error-wrapper">
                        {error && <p className="modal-error">{error}</p>}
                    </div>
                </div>

                {/* FOOTER */}
                <div className="modal-footer">
                    <button className="modal-cancel-btn" onClick={onClose}>
                        Cancel
                    </button>
                    <button
                        className="modal-submit-btn"
                        onClick={handleSubmit}
                        disabled={loading || hasErrors}
                    >
                        {loading ? "Saving..." : "Save"}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default EditMediaForm;