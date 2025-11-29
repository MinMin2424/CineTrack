/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.model.enums;

public enum ValidationMessage {

    USERNAME_REQUIRED("Username is required!"),
    USERNAME_LENGTH("Username must be between 5 and 15 characters!"),
    USERNAME_INVALID_FORMAT("Username can only contain letters, numbers, and underscores!"),
    USERNAME_ALREADY_EXISTS("User with this username: %s already exists!"),

    FIRSTNAME_REQUIRED("First name is required!"),
    FIRSTNAME_LENGTH("First name must be between 3 and 15 characters!"),

    LASTNAME_REQUIRED("Last name is required!"),
    LASTNAME_LENGTH("Last name must be between 3 and 15 characters!"),

    EMAIL_REQUIRED("Email is required!"),
    EMAIL_INVALID_FORMAT("Email should be valid!"),
    EMAIL_ALREADY_EXISTS("User with this email: %s already exists!"),

    AVATAR_REQUIRED("Avatar is required!"),

    PASSWORD_REQUIRED("Password is required!"),
    PASSWORD_LENGTH("Password must be at least 6 characters!"),
    PASSWORD_WEAK("Password is too weak. Must contain at least one uppercase letter, one lowercase letter, and one number!"),

    INVALID_CREDENTIALS("Invalid credentials!"),
    USER_NOT_FOUND("User with this email: %s not found!"),

    REGISTRATION_SUCCESSFUL("Registration successful!"),
    LOGIN_SUCCESSFUL("Login successful!"),

    INVALID_MEDIA_TYPE("Selected type does not match the media type!"),

    START_DATE_REQUIRED("Start date is required!"),
    DATES_REQUIRED("Both date is required for this status!"),
    INVALID_DATES("End date cannot be before start date!"),

    MOVIE_ALREADY_EXISTS("Movie already exists!"),
    SERIES_ALREADY_EXISTS("Series already exists!"),

    TITLE_REQUIRED("Title is required!"),
    RUNTIME_REQUIRED("Runtime is required!"),
    YEAR_REQUIRED("Year is required!"),
    POSTER_REQUIRED("Poster is required!"),
    STATUS_REQUIRED("Status is required!"),
    WATCH_START_DATE_REQUIRED("Watch start date is required!"),
    RATING_REQUIRED("Rating is required!"),
    NOTES_REQUIRED("Notes is required!"),
    SEASON_REQUIRED("Season is required!"),

    INVALID_RATING("Rating must be between 0 and 10!");

    private final String message;

    ValidationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}
