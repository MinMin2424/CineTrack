/*
 * Created by minmin_tranova on 28.04.2026
 */

export const validatePasswordStrength = (password) => {
    if (!password) {
        return "Password is required.";
    }
    if (password.length < 6) {
        return "Password must be at least 6 characters.";
    }
    if (!/[a-z]/.test(password)) {
        return "Password must contain at least one lowercase letter.";
    }
    if (!/[A-Z]/.test(password)) {
        return "Password must contain at least one uppercase letter.";
    }
    if (!/\d/.test(password)) {
        return "Password must contain at least one number.";
    }
    return null;
}