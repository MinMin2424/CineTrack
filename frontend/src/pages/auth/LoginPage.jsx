/*
 * Created by minmin_tranova on 19.02.2026
 */

import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { useFormValidation } from "../../hooks/UseFormValidation";
import '../../styles/pages/auth/LoginPageStyle.css';
import { FiUser } from "react-icons/fi";
import { LuEye } from "react-icons/lu";
import { LuEyeClosed } from "react-icons/lu";

const LoginPage = () => {
    const navigate = useNavigate();
    const { login } = useAuth();
    const { errors, validateLoginForm, setErrors } = useFormValidation();

    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [showPassword, setShowPassword] = useState(false);
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
        const isValid = validateLoginForm(formData);
        if (!isValid) return;

        setIsSubmitting(true);
        try {
            await login(formData.email, formData.password);
            navigate('/');
        } catch (error) {
            const beMessage = error.response?.data?.message || (typeof error.response?.data === 'string' ? error.response?.data : null);
            if (typeof beMessage === 'string') {
                if (beMessage.toLowerCase().includes('email') || beMessage.toLowerCase().includes('user not found')) {
                    setErrors({ email: beMessage });
                } else if (beMessage.toLowerCase().includes('password') || beMessage.toLowerCase().includes('credentials')) {
                    setErrors({ password: 'Incorrect password' });
                } else {
                    setErrors({ general: beMessage });
                }
            } else {
                setErrors({ general: 'Failed to login. Please try again later.' });
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="login-page">
            <img src="/images/spiderman.png" alt="spiderman" className="login-image-spiderman" />
            <img src="/images/studio_ghibli_login.png" alt="studio_ghibli_login" className="login-image-studio_ghibli_login" />

            <div className="login-card">
                <form onSubmit={handleSubmit} noValidate>
                    <img src="/images/wall_e.png" alt="wall_e" className="login-image-wall_e" />

                    <h1 className="login-title">Welcome back!</h1>
                    <p className="login-subtitle">Please login and enjoy your time with CineTrack</p>

                    {errors.general && (<div className="login-error-banner">{errors.general}</div>)}

                    <div className="login-field-wrapper">
                        <div className="login-input-wrapper">
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                value={formData.email}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`login-input ${errors.email ? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="email"
                            />
                            <span className="login-icon-button" style={{pointerEvents: 'none'}}>
                                <FiUser />
                            </span>
                        </div>
                        {errors.email && (<span className="login-error-text">{errors.email}</span>)}
                    </div>

                    <div className="login-field-wrapper">
                        <div className="login-input-wrapper">
                            <input
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                placeholder="Password"
                                value={formData.password}
                                onChange={handleChange}
                                onFocus={handleFocus}
                                className={`login-input ${errors.password? 'error' : ''}`}
                                disabled={isSubmitting}
                                autoComplete="current-password"
                            />
                            <button
                                type="button"
                                className="login-eye-button"
                                onClick={() => setShowPassword((prev) => !prev)}
                                aria-label={showPassword ? 'Hide Password' : 'Show Password'}
                            >
                                {showPassword ? <LuEyeClosed /> : <LuEye />}
                            </button>
                        </div>
                        {errors.password && (<span className="login-error-text">{errors.password}</span>)}
                    </div>

                    <div className="login-forget-wrapper">
                        <Link to="/forgot-password" className="login-forgot-link">
                            Forgot password?
                        </Link>
                    </div>

                    <button
                        type="submit"
                        className="login-submit-button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Log In ...' : 'Login'}
                    </button>

                    <p className="login-switch-text">
                        Don't have an account? {' '}
                        <Link to="/auth/register" className="login-switch-link">
                            Sign up
                        </Link>
                    </p>
                </form>
            </div>

        </div>
    );
};

export default LoginPage;

