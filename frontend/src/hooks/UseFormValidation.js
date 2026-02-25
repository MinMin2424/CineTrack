/*
 * Created by minmin_tranova on 19.02.2026
 * Custom hook for form validation.
 */

import {useState} from "react";

export const useFormValidation = () => {
    const [errors, setErrors] = useState({});

    const validateUsername = (username) => {
        if (!username || username.trim().length === 0)
            return 'Username is required.';
        if (username.trim().length < 5 || username.trim().length > 15)
            return 'Username must be between 5 and 15 characters.';
        if (!/^[a-zA-Z0-9_]+$/.test(username))
            return 'Username must contain only letters, numbers, and _.';
        return null;
    };

    const validateFirstname = (firstname) => {
        if (!firstname || firstname.trim().length === 0)
            return 'First name is required.';
        if (firstname.trim().length < 3 || firstname.trim().length > 15)
            return 'First name must be between 3 and 15 characters.';
        return null;
    };

    const validateLastname = (lastname) => {
        if (!lastname || lastname.trim().length === 0)
            return 'Last name is required.';
        if (lastname.trim().length < 3 || lastname.trim().length > 15)
            return 'Last name must be between 5 and 15 characters.';
        return null;
    };

    const validateEmail = (email) => {
        if (!email || email.trim().length === 0)
            return 'Email is required.';
        if (!/^[A-Za-z0-9+_.-]+@(.+)$/.test(email))
            return 'Invalid email format.';
        return null;
    };

    const validatePassword = (password) => {
        if (!password || password.trim().length === 0)
            return 'Password is required.';
        if (password.trim().length < 6)
            return 'Password must be at least 6 characters long.';
        return null;
    };

    const validateConfirmPassword = (password, confirmPassword) => {
        if (!confirmPassword)
            return 'Confirm password is required.';
        if (password !== confirmPassword)
            return 'Passwords do not match.';
        return null;
    };

    const validateRegisterForm = (formData) => {
        const newErrors = {};

        const usernameError = validateUsername(formData.username);
        if (usernameError) newErrors.username = usernameError;

        const firstnameError = validateFirstname(formData.firstname);
        if (firstnameError) newErrors.firstname = firstnameError;

        const lastnameError = validateLastname(formData.lastname);
        if (lastnameError) newErrors.lastname = lastnameError;

        const emailError = validateEmail(formData.email);
        if (emailError) newErrors.email = emailError;

        const passwordError = validatePassword(formData.password);
        if (passwordError) newErrors.password = passwordError;

        const confirmError = validateConfirmPassword(formData.password, formData.confirmPassword);
        if (confirmError) newErrors.confirmPassword = confirmError;

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const validateLoginForm = (formData) => {
        const newErrors = {};

        const emailError = validateEmail(formData.email);
        if (emailError) newErrors.email = emailError;

        if (!formData.password || formData.password.trim().length === 0) newErrors.password = 'Password is required.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const clearErrors = () => setErrors({});

    return {
        errors,
        setErrors,
        validateRegisterForm,
        validateLoginForm,
        clearErrors,
    };
};