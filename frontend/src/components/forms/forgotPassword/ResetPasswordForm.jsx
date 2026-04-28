/*
 * Created by minmin_tranova on 28.04.2026
 */

import React, {useState} from "react";
import { FaArrowLeft } from "react-icons/fa";
import {LuEye, LuEyeClosed} from "react-icons/lu";
import {Link} from "react-router-dom";

const ResetPasswordForm = ({
    identity,
    resetForm,
    resetErrors,
    resetApiError,
    resetLoading,
    onResetChange,
    onResetSubmit,
    onBackToVerify
}) => {
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    return (
        <>
            <h1 className="fg-title">Set new password</h1>
            <p className="fg-subtitle">
                Account verified. <br/> Choose a new password for
                <strong> {identity.username}.</strong>
            </p>

            <form className="fg-form" onSubmit={onResetSubmit} noValidate>
                <div className="fg-field-wrapper">
                    <label className="fg-label">
                        New password <span className="required-star">*</span>
                    </label>
                    <img src="/images/cards.png" alt="cards" className="cards"/>
                    <div className="fg-input-wrapper">
                        <input
                            type={showPassword ? "text" : "password"}
                            name="password"
                            value={resetForm.password}
                            onChange={onResetChange}
                            placeholder="Enter new password"
                            className={`fg-input ${resetErrors.password ? 'input-error' : ''}`}
                        />
                        <button
                            type="button"
                            className="fg-eye-button"
                            onClick={() => setShowPassword((p) => !p)}
                            aria-label={showPassword ? 'Hide Password' : 'Show Password'}
                        >
                            {showPassword ? <LuEyeClosed /> : <LuEye />}
                        </button>
                    </div>
                    {resetErrors.password && (
                        <p className="fg-field-error">{resetErrors.password}</p>
                    )}
                </div>

                <div className="fg-field-wrapper">
                    <label className="fg-label">
                        Confirm new password <span className="required-star">*</span>
                    </label>
                    <div className="fg-input-wrapper">
                        <input
                            type={showConfirmPassword ? "text" : "password"}
                            name="confirmPassword"
                            value={resetForm.confirmPassword}
                            onChange={onResetChange}
                            placeholder="Re-enter new password"
                            className={`fg-input ${resetErrors.confirmPassword ? 'input-error' : ''}`}
                        />
                        <button
                            type="button"
                            className="fg-eye-button"
                            onClick={() => setShowConfirmPassword((p) => !p)}
                            aria-label={showConfirmPassword ? 'Hide Password' : 'Show Password'}
                        >
                            {showConfirmPassword ? <LuEyeClosed /> : <LuEye />}
                        </button>
                    </div>
                    {resetErrors.confirmPassword && (
                        <p className="fg-field-error">{resetErrors.confirmPassword}</p>
                    )}
                </div>
                {resetApiError && (
                    <p className="fg-api-error">{resetApiError}</p>
                )}
                <button
                    type="submit"
                    className="fg-submit-btn"
                    disabled={resetLoading}
                >
                    {resetLoading ? "Saving..." : "Reset password"}
                </button>
            </form>
            <button className="fg-back-link" onClick={onBackToVerify}>
                <FaArrowLeft /> Back
            </button>
        </>
    );
};

export default ResetPasswordForm;