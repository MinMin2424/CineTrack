/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.utils;

import cz.cvut.fel.cinetrack.dto.auth.LoginRequest;
import cz.cvut.fel.cinetrack.dto.auth.RegisterRequest;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequest;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequest;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequest;
import cz.cvut.fel.cinetrack.exception.PasswordNotStrongEnoughException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.AvatarCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.EmailCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.FirstnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.LastnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.PasswordCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.cannotBeNullExceptions.UsernameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.invalidFormatExceptions.InvalidEmailFormatException;
import cz.cvut.fel.cinetrack.exception.invalidFormatExceptions.InvalidFirstnameLengthException;
import cz.cvut.fel.cinetrack.exception.invalidFormatExceptions.InvalidLastnameLengthException;
import cz.cvut.fel.cinetrack.exception.invalidFormatExceptions.InvalidUsernameFormatException;
import cz.cvut.fel.cinetrack.exception.invalidFormatExceptions.InvalidUsernameLengthException;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateRegisterRequest(RegisterRequest registerRequest) {
        validateRequiredFields(registerRequest);
        validateUsernameLength(registerRequest);
        validateFirstnameLength(registerRequest);
        validateLastnameLength(registerRequest);
        validateFieldFormats(registerRequest);
        validatePasswordStrength(registerRequest);
    }

    public void validateLoginRequest(LoginRequest loginRequest) {
        validateRequiredFields(loginRequest);
    }

    public void validateEditProfileData(EditUserProfileRequest editUserProfileRequest) {
        validateRequiredFields(editUserProfileRequest);
        validateFieldFormats(editUserProfileRequest);
        validateUsernameLength(editUserProfileRequest);
        validateFirstnameLength(editUserProfileRequest);
        validateLastnameLength(editUserProfileRequest);
    }

    public void validateUserPassword(ChangeUserPasswordRequest changeUserPasswordRequest) {
        validatePasswordStrength(changeUserPasswordRequest);
    }

    public void validateUserAvatar(ChangeUserAvatarRequest changeUserAvatarRequest) {
        validateAvatar(changeUserAvatarRequest);
    }

    private void validateRequiredFields(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty()) {
            throw new UsernameCannotBeNullException(ValidationMessage.USERNAME_REQUIRED.getMessage());
        }
        if (registerRequest.getFirstname() == null || registerRequest.getFirstname().isEmpty()) {
            throw new FirstnameCannotBeNullException(ValidationMessage.FIRSTNAME_REQUIRED.getMessage());
        }
        if (registerRequest.getLastname() == null || registerRequest.getLastname().isEmpty()) {
            throw new LastnameCannotBeNullException(ValidationMessage.LASTNAME_REQUIRED.getMessage());
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            throw new EmailCannotBeNullException(ValidationMessage.EMAIL_REQUIRED.getMessage());
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            throw new PasswordCannotBeNullException(ValidationMessage.PASSWORD_REQUIRED.getMessage());
        }
    }

    private void validateRequiredFields(LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            throw new EmailCannotBeNullException(ValidationMessage.EMAIL_REQUIRED.getMessage());
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new PasswordCannotBeNullException(ValidationMessage.PASSWORD_REQUIRED.getMessage());
        }
    }

    private void validateRequiredFields(EditUserProfileRequest editUserProfileRequest) {
        if (editUserProfileRequest.getUsername() == null || editUserProfileRequest.getUsername().isEmpty()) {
            throw new UsernameCannotBeNullException(ValidationMessage.USERNAME_REQUIRED.getMessage());
        }
        if (editUserProfileRequest.getFirstname() == null || editUserProfileRequest.getFirstname().isEmpty()) {
            throw new FirstnameCannotBeNullException(ValidationMessage.FIRSTNAME_REQUIRED.getMessage());
        }
        if (editUserProfileRequest.getLastname() == null || editUserProfileRequest.getLastname().isEmpty()) {
            throw new LastnameCannotBeNullException(ValidationMessage.LASTNAME_REQUIRED.getMessage());
        }
        if (editUserProfileRequest.getEmail() == null || editUserProfileRequest.getEmail().trim().isEmpty()) {
            throw new EmailCannotBeNullException(ValidationMessage.EMAIL_REQUIRED.getMessage());
        }
    }

    private void validateFieldFormats(RegisterRequest registerRequest) {
        if (!isValidUsername(registerRequest.getUsername())) {
            throw new InvalidUsernameFormatException(ValidationMessage.USERNAME_INVALID_FORMAT.getMessage());
        }
        if (!isValidEmail(registerRequest.getEmail())) {
            throw new InvalidEmailFormatException(ValidationMessage.EMAIL_INVALID_FORMAT.getMessage());
        }
    }

    private void validateFieldFormats(EditUserProfileRequest editUserProfileRequest) {
        if (!isValidUsername(editUserProfileRequest.getUsername())) {
            throw new InvalidUsernameFormatException(ValidationMessage.USERNAME_INVALID_FORMAT.getMessage());
        }
        if (!isValidEmail(editUserProfileRequest.getEmail())) {
            throw new InvalidEmailFormatException(ValidationMessage.EMAIL_INVALID_FORMAT.getMessage());
        }
    }

    private void validateUsernameLength(RegisterRequest registerRequest) {
        if (registerRequest.getUsername().trim().length() < 5 || registerRequest.getUsername().trim().length() > 15) {
            throw new InvalidUsernameLengthException(ValidationMessage.USERNAME_LENGTH.getMessage());
        }
    }

    private void validateUsernameLength(EditUserProfileRequest editUserProfileRequest) {
        if (editUserProfileRequest.getUsername().trim().length() < 5 || editUserProfileRequest.getUsername().trim().length() > 15) {
            throw new InvalidUsernameLengthException(ValidationMessage.USERNAME_LENGTH.getMessage());
        }
    }

    private void validateFirstnameLength(RegisterRequest registerRequest) {
        if (registerRequest.getFirstname().trim().length() < 3 || registerRequest.getFirstname().trim().length() > 15) {
            throw new InvalidFirstnameLengthException(ValidationMessage.FIRSTNAME_LENGTH.getMessage());
        }
    }

    private void validateFirstnameLength(EditUserProfileRequest editUserProfileRequest) {
        if (editUserProfileRequest.getFirstname().trim().length() < 3 || editUserProfileRequest.getFirstname().trim().length() > 15) {
            throw new InvalidFirstnameLengthException(ValidationMessage.FIRSTNAME_LENGTH.getMessage());
        }
    }


    private void validateLastnameLength(RegisterRequest registerRequest) {
        if (registerRequest.getLastname().trim().length() < 3 || registerRequest.getLastname().trim().length() > 15) {
            throw new InvalidLastnameLengthException(ValidationMessage.LASTNAME_LENGTH.getMessage());
        }
    }

    private void validateLastnameLength(EditUserProfileRequest editUserProfileRequest) {
        if (editUserProfileRequest.getLastname().trim().length() < 3 || editUserProfileRequest.getLastname().trim().length() > 15) {
            throw new InvalidLastnameLengthException(ValidationMessage.LASTNAME_LENGTH.getMessage());
        }
    }

    private void validatePasswordStrength(RegisterRequest registerRequest) {
        if (registerRequest.getPassword().trim().length() < 6) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_LENGTH.getMessage());
        }
        if (!isPasswordStrong(registerRequest.getPassword())) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_WEAK.getMessage());
        }
    }

    private void validatePasswordStrength(ChangeUserPasswordRequest changeUserPasswordRequest) {
        if (changeUserPasswordRequest.getPassword() == null) {
            throw new PasswordCannotBeNullException(ValidationMessage.PASSWORD_REQUIRED.getMessage());
        }
        if (changeUserPasswordRequest.getPassword().trim().length() < 6) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_LENGTH.getMessage());
        }
        if (!isPasswordStrong(changeUserPasswordRequest.getPassword())) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_WEAK.getMessage());
        }
    }

    private void validateAvatar(ChangeUserAvatarRequest changeUserAvatarRequest) {
        if (changeUserAvatarRequest.getAvatar() == null || changeUserAvatarRequest.getAvatar().trim().isEmpty()) {
            throw new AvatarCannotBeNullException(ValidationMessage.AVATAR_REQUIRED.getMessage());
        }
    }

    private boolean isValidUsername(String username) {
        String usernameRegex = "^[a-zA-Z0-9_]+$";
        return username != null && username.matches(usernameRegex);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isPasswordStrong(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
        return password != null && password.matches(passwordRegex);
    }

}
