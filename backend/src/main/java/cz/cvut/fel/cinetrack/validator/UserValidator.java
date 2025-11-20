/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.validator;

import cz.cvut.fel.cinetrack.dto.auth.LoginRequest;
import cz.cvut.fel.cinetrack.dto.auth.RegisterRequest;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequestDTO;
import cz.cvut.fel.cinetrack.exception.user.PasswordNotStrongEnoughException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.AvatarCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.EmailCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.FirstnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.LastnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.PasswordCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.nonNullData.UsernameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.user.invalidFormat.InvalidEmailFormatException;
import cz.cvut.fel.cinetrack.exception.user.invalidFormat.InvalidFirstnameLengthException;
import cz.cvut.fel.cinetrack.exception.user.invalidFormat.InvalidLastnameLengthException;
import cz.cvut.fel.cinetrack.exception.user.invalidFormat.InvalidUsernameFormatException;
import cz.cvut.fel.cinetrack.exception.user.invalidFormat.InvalidUsernameLengthException;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateRegisterRequest(RegisterRequest registerRequest) {
        validateRequiredFields(registerRequest.getUsername(),
                                registerRequest.getFirstname(),
                                registerRequest.getLastname(),
                                registerRequest.getEmail());
        validateUsernameLength(registerRequest.getUsername());
        validateFirstnameLength(registerRequest.getFirstname());
        validateLastnameLength(registerRequest.getLastname());
        validateFieldFormats(registerRequest.getUsername(), registerRequest.getEmail());
        validatePasswordStrength(registerRequest.getPassword());
    }

    public void validateLoginRequest(LoginRequest loginRequest) {
        validateRequiredFields(loginRequest);
    }

    public void validateEditProfileData(EditUserProfileRequestDTO editUserProfileRequest) {
        validateRequiredFields(editUserProfileRequest.getUsername(),
                                editUserProfileRequest.getFirstname(),
                                editUserProfileRequest.getLastname(),
                                editUserProfileRequest.getEmail());
        validateFieldFormats(editUserProfileRequest.getUsername(), editUserProfileRequest.getEmail());
        validateUsernameLength(editUserProfileRequest.getUsername());
        validateFirstnameLength(editUserProfileRequest.getFirstname());
        validateLastnameLength(editUserProfileRequest.getLastname());
    }

    public void validateUserPassword(ChangeUserPasswordRequestDTO changeUserPasswordRequestDTO) {
        validatePasswordStrength(changeUserPasswordRequestDTO.getPassword());
    }

    public void validateUserAvatar(ChangeUserAvatarRequestDTO changeUserAvatarRequestDTO) {
        validateAvatar(changeUserAvatarRequestDTO);
    }

    private void validateRequiredFields(String username,
                                        String firstname,
                                        String lastname,
                                        String password) {
        if (username == null || username.isEmpty()) {
            throw new UsernameCannotBeNullException(ValidationMessage.USERNAME_REQUIRED.getMessage());
        }
        if (firstname == null || firstname.isEmpty()) {
            throw new FirstnameCannotBeNullException(ValidationMessage.FIRSTNAME_REQUIRED.getMessage());
        }
        if (lastname == null || lastname.isEmpty()) {
            throw new LastnameCannotBeNullException(ValidationMessage.LASTNAME_REQUIRED.getMessage());
        }
        if (password == null || password.trim().isEmpty()) {
            throw new EmailCannotBeNullException(ValidationMessage.EMAIL_REQUIRED.getMessage());
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

    private void validateFieldFormats(String username, String email) {
        if (!isValidUsername(username)) {
            throw new InvalidUsernameFormatException(ValidationMessage.USERNAME_INVALID_FORMAT.getMessage());
        }
        if (!isValidEmail(email)) {
            throw new InvalidEmailFormatException(ValidationMessage.EMAIL_INVALID_FORMAT.getMessage());
        }
    }

    private void validateUsernameLength(String username) {
        if (username.trim().length() < 5 || username.trim().length() > 15) {
            throw new InvalidUsernameLengthException(ValidationMessage.USERNAME_LENGTH.getMessage());
        }
    }

    private void validateFirstnameLength(String firstname) {
        if (firstname.trim().length() < 3 || firstname.trim().length() > 15) {
            throw new InvalidFirstnameLengthException(ValidationMessage.FIRSTNAME_LENGTH.getMessage());
        }
    }

    private void validateLastnameLength(String lastname) {
        if (lastname.trim().length() < 3 || lastname.trim().length() > 15) {
            throw new InvalidLastnameLengthException(ValidationMessage.LASTNAME_LENGTH.getMessage());
        }
    }

    private void validatePasswordStrength(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordCannotBeNullException(ValidationMessage.PASSWORD_REQUIRED.getMessage());
        }
        if (password.trim().length() < 6) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_LENGTH.getMessage());
        }
        if (!isPasswordStrong(password)) {
            throw new PasswordNotStrongEnoughException(ValidationMessage.PASSWORD_WEAK.getMessage());
        }
    }

    private void validateAvatar(ChangeUserAvatarRequestDTO changeUserAvatarRequestDTO) {
        if (changeUserAvatarRequestDTO.getAvatar() == null || changeUserAvatarRequestDTO.getAvatar().trim().isEmpty()) {
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
