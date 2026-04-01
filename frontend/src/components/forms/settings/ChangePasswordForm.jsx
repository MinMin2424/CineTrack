/*
 * Created by minmin_tranova on 01.04.2026
 */

import React from 'react';
import InputField from "../InputField";
import "../../../styles/components/forms/AddMediaFormStyle.css"

const ChangePasswordForm = ({
    passwordForm,
    passwordErrors,
    passwordApiError,
    passwordLoading,
    onPasswordChange,
    onPasswordSubmit,
    onClose,
}) => {
    return (
        <>
            <div className="modal-body">
                <InputField
                    label="Current password"
                    required
                    type="password"
                    name="currentPassword"
                    value={passwordForm.currentPassword}
                    onChange={onPasswordChange}
                    error={passwordErrors.currentPassword}
                    placeholder="Enter your current password"
                />
                <InputField
                    label="New password"
                    required
                    type="password"
                    name="password"
                    value={passwordForm.password}
                    onChange={onPasswordChange}
                    error={passwordErrors.password}
                    placeholder="Enter your new password"
                />
                <InputField
                    label="Confirm new password"
                    required
                    type="password"
                    name="confirmPassword"
                    value={passwordForm.confirmPassword}
                    onChange={onPasswordChange}
                    error={passwordErrors.confirmPassword}
                    placeholder="Re-enter your new password"
                />

                <div className="modal-error-wrapper">
                    {passwordApiError && <p className="modal-error">{passwordApiError}</p>}
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
                    onClick={onPasswordSubmit}
                    disabled={passwordLoading}
                >
                    {passwordLoading ? "Saving..." : "Save"}
                </button>
            </div>
        </>
    );
};

export default ChangePasswordForm;