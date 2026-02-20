/*
 * Created by minmin_tranova on 19.02.2026
 */

import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { useFormValidation } from "../../hooks/UseFormValidation";
import "../../styles/pages/auth/RegisterPageStyle.css"
import { LuEye } from "react-icons/lu";
import { LuEyeClosed } from "react-icons/lu";

const RegisterPage = () => {
    const navigate = useNavigate();
    const { register } = useAuth();
    const { errors, validateRegisterForm, setErrors } = useFormValidation();

    const [formData, setFormData] = useState({
        username: '',
        firstname: '',
        lastname: '',
        email: '',
        password: '',
        confirmPassword: '',
    });
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: null }));
        }
    };
    const handleFocus = (fieldName) => {
        if (errors.general) {
            setErrors((prev) => ({ ...prev, general: null }));
        }
    }
    const handleSubmit = async (e) => {
        e.preventDefault();
        const isValid = validateRegisterForm(formData);
        if (!isValid) return;

        setIsSubmitting(true);
        try {
            const { confirmPassword, ...registerData} = formData;
            await register(registerData);
            navigate('/');
        } catch (error) {
            const beMessage = error.response?.data?.message || error.response?.data;
            if (typeof beMessage === 'string') {
                if (beMessage.toLowerCase().includes('username')) {
                    setErrors({ username: beMessage });
                } else if (beMessage.toLowerCase().includes('email')) {
                    setErrors({ email: beMessage });
                } else if (beMessage.toLowerCase().includes('password')) {
                    setErrors({ password: beMessage });
                } else {
                    setErrors({ general: beMessage });
                }
            } else {
                setErrors({ general: 'Failed to register. Please try again.' });
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="register-page">
            <img src="/images/minions.png" alt="minions" className="register-image-minions" />
            <img src="/images/toroto.png" alt="toroto" className="register-image-toroto" />

            <div className="register-card">
                <form onSubmit={handleSubmit} noValidate>
                    <h1 className="register-title">Register</h1>

                    {errors.general && <div className="register-error-banner">{errors.general}</div>}

                    <div className="register-field-wrapper">
                        <div className="register-input-wrapper">
                            <input
                                type="text"
                                name="username"
                                placeholder="Username (5-15 characters)"
                                value={formData.username}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`register-input ${errors.username ? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="username"
                            />
                        </div>
                        {errors.username && <span className="register-error-text">{errors.username}</span>}
                    </div>

                    <div className="register-row-wrapper">
                        <div className="register-field-wrapper">
                            <div className="register-input-wrapper">
                                <input
                                    type="text"
                                    name="firstname"
                                    placeholder="First Name"
                                    value={formData.firstname}
                                    onChange={handleChange}
                                    onFocus={handleFocus}
                                    className={`register-input ${errors.firstname ? 'error' : ''}`}
                                    disabled={isSubmitting}
                                    autoComplete="given-name"
                                />
                            </div>
                            {errors.firstname && <span className="register-error-text">{errors.firstname}</span>}
                        </div>
                        <div className="register-field-wrapper">
                            <div className="register-input-wrapper">
                                <input
                                    type="text"
                                    name="lastname"
                                    placeholder="Last Name"
                                    value={formData.lastname}
                                    onChange={handleChange}
                                    onFocus={handleFocus}
                                    className={`register-input ${errors.lastname ? 'error' : ''}`}
                                    disabled={isSubmitting}
                                    autoComplete="family-name"
                                />
                            </div>
                            {errors.lastname && <span className="register-error-text">{errors.lastname}</span>}
                        </div>
                    </div>

                    <div className="register-field-wrapper">
                        <div className="register-input-wrapper">
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                value={formData.email}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`register-input ${errors.email ? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="email"
                            />
                        </div>
                        {errors.email && <span className="register-error-text">{errors.email}</span>}
                    </div>

                    <div className="register-field-wrapper">
                        <div className="register-input-wrapper">
                            <input
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                placeholder="Password (min. 6 characters)"
                                value={formData.password}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`register-input ${errors.password? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="new-password"
                            />
                            <button
                                type="button"
                                className="register-eye-button"
                                onClick={() => setShowPassword((p) => !p)}
                                aria-label={showPassword ? 'Hide Password' : 'Show Password'}
                            >
                                {showPassword ? <LuEyeClosed /> : <LuEye />}
                            </button>
                        </div>
                        {errors.password && <span className="register-error-text">{errors.password}</span>}
                    </div>

                    <div className="register-field-wrapper">
                        <div className="register-input-wrapper">
                            <input
                                type={showConfirmPassword ? 'text' : 'password'}
                                name="confirmPassword"
                                placeholder="Confirm Password"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`register-input ${errors.confirmPassword? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="new-password"
                            />
                            <button
                                type="button"
                                className="register-eye-button"
                                onClick={() => setShowConfirmPassword((p) => !p)}
                                aria-label={showConfirmPassword ? 'Hide Password' : 'Show Password'}
                            >
                                {showConfirmPassword ? <LuEyeClosed /> : <LuEye />}
                            </button>
                        </div>
                        {errors.confirmPassword && <span className="register-error-text">{errors.confirmPassword}</span>}
                    </div>

                    <button
                        type="submit"
                        className="register-submit-button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Submitting ...' : 'Register'}
                    </button>

                    <p className="register-switch-text">
                        Already have an account?{' '}
                        <Link to="/auth/login" className="register-switch-link">
                            Log in
                        </Link>
                    </p>
                </form>
            </div>
        </div>
    );
};

export default RegisterPage;