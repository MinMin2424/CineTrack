/*
 * Created by minmin_tranova on 28.04.2026
 */

import React from "react";
import {Link} from 'react-router-dom';

const VerifyIdentityForm = ({
    verifyForm,
    verifyErrors,
    verifyApiError,
    verifyLoading,
    onVerifyChange,
    onVerifySubmit
}) => {
    return (
        <>
            <h1 className="fg-title">Forgot password?</h1>
            <p className="fg-subtitle">
                Enter your email and username to verify your account.
            </p>

            <form className="fg-form" onSubmit={onVerifySubmit} noValidate>
                <div className="fg-field-wrapper">
                    <img src="/images/cards.png" alt="cards" className="cards"/>
                    <label className="fg-label">Email <span className="required-star"> *</span></label>
                    <div className="fg-input-wrapper">
                        <input
                            type="email"
                            name="email"
                            placeholder="Enter your email"
                            value={verifyForm.email}
                            onChange={onVerifyChange}
                            className={`fg-input ${verifyErrors.email ? 'input-error' : ''}`}
                        />
                    </div>
                    {verifyErrors.email && <span className="fg-field-error">{verifyErrors.email}</span>}
                </div>
                <div className="fg-field-wrapper">
                    <label className="fg-label">Username <span className="required-star"> *</span></label>
                    <div className="fg-input-wrapper">
                        <input
                            type="text"
                            name="username"
                            placeholder="Enter your username"
                            value={verifyForm.username}
                            onChange={onVerifyChange}
                            className={`fg-input ${verifyErrors.username ? 'input-error' : ''}`}
                        />
                    </div>
                    {verifyErrors.username && <span className="fg-field-error">{verifyErrors.username}</span>}
                </div>
                {verifyApiError && (
                    <p className="fg-api-error">{verifyApiError}</p>
                )}
                <button
                    type="submit"
                    className="fg-submit-btn"
                    disabled={verifyLoading}
                >
                    {verifyLoading ? "Verifying..." : "Continue"}
                </button>
            </form>
            <p className="fg-back-link">
                Remembered it?
                <Link to="/auth/login" className="fg-link">
                    Back to login
                </Link>
            </p>
        </>
    );
};

export default VerifyIdentityForm;