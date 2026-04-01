/*
 * Created by minmin_tranova on 26.03.2026
 */

import React from "react";
import InputField from "../InputField";
import "../../../styles/components/forms/AddMediaFormStyle.css"

const EditUserProfileForm = ({
    editForm,
    editErrors,
    editApiError,
    editLoading,
    onEditChange,
    onEditSubmit,
    onClose,
}) => {
    return (
        <>
            <div className="modal-body">
                <div className="modal-row">
                    <InputField
                        label="First name"
                        required
                        name="firstname"
                        value={editForm.firstname}
                        onChange={onEditChange}
                        error={editErrors.firstname}
                    />
                    <InputField
                        label="Last name"
                        required
                        name="lastname"
                        value={editForm.lastname}
                        onChange={onEditChange}
                        error={editErrors.lastname}
                    />
                </div>

                <InputField
                    label="Username"
                    required
                    name="username"
                    value={editForm.username}
                    onChange={onEditChange}
                    error={editErrors.username}
                />
                
                <InputField
                    label="Email"
                    required
                    name="email"
                    value={editForm.email}
                    onChange={onEditChange}
                    error={editErrors.email}
                />

                <div className="modal-error-wrapper">
                    {editApiError && <p className="modal-error">{editApiError}</p>}
                </div>
            </div>

            <div className="modal-footer">
                <button
                    className="modal-cancel-btn"
                    onClick={onClose}
                >
                    Cancel
                </button>
                <button
                    className="modal-submit-btn"
                    onClick={onEditSubmit}
                    disabled={editLoading}
                >
                    {editLoading ? "Saving..." : "Save"}
                </button>
            </div>
        </>
    );
};

export default EditUserProfileForm;