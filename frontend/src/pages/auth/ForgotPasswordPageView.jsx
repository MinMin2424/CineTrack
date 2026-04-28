/*
 * Created by minmin_tranova on 28.04.2026
 */

import React from "react";
import {STEP_VERIFY, STEP_RESET, STEP_DONE} from "./ForgotPasswordPageContainer";
import VerifyIdentityForm from "../../components/forms/forgotPassword/VerifyIdentityForm";
import { MdDone } from "react-icons/md";
import { FaArrowRight } from "react-icons/fa";
import "../../styles/pages/auth/ForgotPasswordPageStyle.css"
import ResetPasswordForm from "../../components/forms/forgotPassword/ResetPasswordForm";
import "../../styles/components/forms/AddMediaFormStyle.css"
import "../../styles/mobile-version/ForgotPasswordMobileStyle.css"

const ForgotPasswordPageView = ({
    step,
    identity,
    verifyForm, verifyErrors, verifyApiError, verifyLoading,
    onVerifyChange, onVerifySubmit,
    resetForm, resetErrors, resetApiError, resetLoading,
    onResetChange, onResetSubmit,
    onBackToVerify, onGoToLogin
}) => {
    return (
        <div className="fg-page">
            {step === STEP_VERIFY && (
                <img src="/images/red-queen.png" alt="red-queen" className="red-queen"/>
            )}
            {step === STEP_RESET && (
                <img src="/images/mad-hatter.png" alt="mad-hatter" className="mad-hatter"/>
            )}

            <div className="fg-card">
                {step === STEP_VERIFY && (
                    <VerifyIdentityForm
                        verifyForm={verifyForm}
                        verifyErrors={verifyErrors}
                        verifyApiError={verifyApiError}
                        verifyLoading={verifyLoading}
                        onVerifyChange={onVerifyChange}
                        onVerifySubmit={onVerifySubmit}
                    />
                )}
                {step === STEP_RESET && (
                    <ResetPasswordForm
                        identity={identity}
                        resetForm={resetForm}
                        resetErrors={resetErrors}
                        resetApiError={resetApiError}
                        resetLoading={resetLoading}
                        onResetChange={onResetChange}
                        onResetSubmit={onResetSubmit}
                        onBackToVerify={onBackToVerify}
                    />
                )}
                {step === STEP_DONE && (
                    <>
                        <img src="/images/chesire-cat.png" alt="chesire-cat" className="chesire-cat"/>
                        <div className="fg-success">
                            <h2 className="fg-title">Password changed!</h2>
                            <p className="fg-subtitle">
                                Your password has been successfully changed! <br/>
                                You can now log in with your new password.
                            </p>
                            <button className="fg-submit-btn" onClick={onGoToLogin}>
                                Go to login <FaArrowRight />
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default ForgotPasswordPageView;