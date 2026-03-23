/*
 * Created by minmin_tranova on 15.03.2026
 */

import React from "react";
import {IoClose} from "react-icons/io5";
import {IoIosArrowDown} from "react-icons/io";
import {useMediaFormValidation} from "../../../hooks/UseMediaFormValidation";
import InputField from "../InputField";
import "../../../styles/components/forms/AddMediaFormStyle.css"

const STATUS_OPTIONS = [
    { value: "plan to watch", label: "Plan to Watch" },
    { value: "watching", label: "Watching" },
    { value: "completed", label: "Completed" },
    { value: "dropped", label: "Dropped" },
];

const EditEpisodeForm = ({
    formData,
    onChange,
    onSubmit,
    onClose,
    loading,
    error,
}) => {
    const {fieldErrors} = useMediaFormValidation(formData, ["rating"]);
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
                  <h2 className="modal-title">Edit Episode</h2>
                  <button className="modal-close-btn" onClick={onClose}>
                      <IoClose />
                  </button>
              </div>
              {/* BODY */}
              <div className="modal-body">
                  <div className="modal-row">
                      <div className="modal-field">
                          <label className="modal-label">
                              Watch status
                              <span className="required-star"> *</span>
                          </label>
                          <div className="modal-select-wrapper">
                              <InputField
                                  type="select"
                                  name="status"
                                  value={formData.status || ""}
                                  onChange={onChange}
                              >
                                  {STATUS_OPTIONS.map(s => (
                                      <option key={s.value} value={s.value}>{s.label}</option>
                                  ))}
                              </InputField>
                              <IoIosArrowDown className="modal-select-icon"/>
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
                          value={formData.rating ?? ""}
                          onChange={onChange}
                          error={fieldErrors.rating}
                      />
                  </div>
                  <InputField
                      label="Personal notes"
                      type="textarea"
                      name="notes"
                      placeholder="Add your thoughts, reviews, reminders, ..."
                      rows="4"
                      value={formData.notes ?? ""}
                      onChange={onChange}
                  />
                  <div className="modal-error-warpper">
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

export default EditEpisodeForm;