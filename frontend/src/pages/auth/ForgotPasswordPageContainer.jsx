/*
 * Created by minmin_tranova on 28.04.2026
 */

import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import {verifyUserForReset, resetPassword} from "../../api/AuthApi";
import ForgotPasswordPageView from "./ForgotPasswordPageView";
import {validatePasswordStrength} from "../../utils/ValidatePasswordStrength";

export const STEP_VERIFY = 1;
export const STEP_RESET = 2;
export const STEP_DONE = 3;

const ForgotPasswordPageContainer = () => {
    const navigate = useNavigate();
    const [step, setStep] = useState(STEP_VERIFY);
    const [identity, setIdentity] = useState({
        email: "", username: ""
    });
    // STEP 1
    const [verifyForm, setVerifyForm] = useState({
        email: "", username: ""
    });
    const [verifyErrors, setVerifyErrors] = useState({});
    const [verifyApiError, setVerifyApiError] = useState("");
    const [verifyLoading, setVerifyLoading] = useState(false);
    // STEP 2
    const [resetForm, setResetForm] = useState({
        password: "", confirmPassword: ""
    });
    const [resetErrors, setResetErrors] = useState({});
    const [resetApiError, setResetApiError] = useState("");
    const [resetLoading, setResetLoading] = useState(false);

    const handleVerifyChange = (e) => {
        const {name, value} = e.target;
        setVerifyForm(prev => ({...prev, [name]: value}));
        setVerifyErrors(prev => ({...prev, [name]: ""}));
        setVerifyApiError("");
    };

    const validateVerify = () => {
        const errs = {};
        if (!verifyForm.email.trim()) errs.email = "Email is required.";
        else if (!/\S+@\S+\.\S+/.test(verifyForm.email)) errs.email = "Invalid email format.";
        if (!verifyForm.username.trim()) errs.username = "Username is required.";
        return errs;
    };

    const handleVerifySubmit = async (e) => {
        e.preventDefault();
        const errs = validateVerify();
        if (Object.keys(errs).length) {
            setVerifyErrors(errs);
            return;
        }
        setVerifyLoading(true);
        setVerifyApiError("");
        try {
            await verifyUserForReset(verifyForm);
            setIdentity({
                email: verifyForm.email, username: verifyForm.username
            });
            setStep(STEP_RESET)
        } catch (err) {
            const status = err?.response?.status;
            setVerifyApiError(
                status === 404
                ? "No account found with this email and username."
                : err?.response?.data?.message || "Something went wrong. Please try again."
            );
        } finally {
            setVerifyLoading(false);
        }
    };

    const handleResetChange = (e) => {
        const {name, value} = e.target;
        setResetForm(prev => ({...prev, [name]: value}));
        setResetErrors(prev => ({...prev, [name]: ""}));
        setResetApiError("");
    };

    const validateReset = () => {
        const errs = {};
        const passwordError = validatePasswordStrength(resetForm.password);
        if (passwordError) errs.password = passwordError;
        if (!resetForm.confirmPassword) {
            errs.confirmPassword = "Please confirm your password.";
        } else if (resetForm.password !== resetForm.confirmPassword) {
            errs.confirmPassword = "Passwords do not match.";
        }
        return errs;
    };

    const handleResetSubmit = async (e) => {
        e.preventDefault();
        const errs = validateReset();
        if (Object.keys(errs).length) {
            setResetErrors(errs);
            return;
        }
        setResetLoading(true);
        setResetApiError("");
        try {
            await resetPassword({
                email: identity.email,
                username: identity.username,
                password: resetForm.password,
                confirmPassword: resetForm.confirmPassword
            });
            setStep(STEP_DONE);
        } catch (err) {
            setResetApiError(
                err?.response?.data?.message || "Failed to reset password. Please try again."
            );
        } finally {
            setResetLoading(false);
        }
    };

    const handleBackToVerify = () => setStep(STEP_VERIFY);
    const handleGoToLogin = () => navigate("/auth/login", {replace: true});

    return (
        <ForgotPasswordPageView
            step={step}
            identity={identity}

            verifyForm={verifyForm}
            verifyErrors={verifyErrors}
            verifyApiError={verifyApiError}
            verifyLoading={verifyLoading}
            onVerifyChange={handleVerifyChange}
            onVerifySubmit={handleVerifySubmit}

            resetForm={resetForm}
            resetErrors={resetErrors}
            resetApiError={resetApiError}
            resetLoading={resetLoading}
            onResetChange={handleResetChange}
            onResetSubmit={handleResetSubmit}

            onBackToVerify={handleBackToVerify}
            onGoToLogin={handleGoToLogin}
        />
    );
};

export default ForgotPasswordPageContainer;