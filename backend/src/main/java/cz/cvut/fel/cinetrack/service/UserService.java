/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequest;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequest;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequest;
import cz.cvut.fel.cinetrack.dto.user.response.CombinedUserProfile;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileHeaderResponse;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileResponse;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.UsernameAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    public UserProfileResponse getCurrentUserProfile() {
        User currentUser = SecurityUtils.getCurrentUser();
        return new UserProfileResponse(
                currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getEmail()
        );
    }

    public UserProfileHeaderResponse getCurrentUserProfileHeader() {
        User currentUser = SecurityUtils.getCurrentUser();
        return new UserProfileHeaderResponse(
                "@" + currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getCreationDate(),
                currentUser.getAvatar()
        );
    }

    public CombinedUserProfile getCombinedUserProfile() {
        return new CombinedUserProfile(
                getCurrentUserProfile(),
                getCurrentUserProfileHeader()
        );
    }

    public UserProfileResponse editUserProfile(EditUserProfileRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        userValidator.validateEditProfileData(request);
        validateUniqueness(request, currentUser.getId());

        currentUser.setUsername(request.getUsername());
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setModified(LocalDateTime.now());

        User updatedUser = userRepository.save(currentUser);
        return new UserProfileResponse(
                updatedUser.getUsername(),
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail()
        );
    }

    public void changeUserPassword(ChangeUserPasswordRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        userValidator.validateUserPassword(request);
        currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        currentUser.setModified(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    public void changeUserAvatar(ChangeUserAvatarRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        userValidator.validateUserAvatar(request);
        currentUser.setAvatar(request.getAvatar());
        currentUser.setModified(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    private void validateUniqueness(EditUserProfileRequest request, Long currentUserId) {
        Optional<User> existingUserByUsername = userRepository.findByUsername(request.getUsername());
        if (existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(currentUserId)) {
            throw new UsernameAlreadyExistsException(
                    ValidationMessage.USERNAME_ALREADY_EXISTS.getFormattedMessage(request.getUsername())
            );
        }
        Optional<User> existingUserByEmail = userRepository.findByEmail(request.getEmail());
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(currentUserId)) {
            throw new EmailAlreadyExistsException(
                    ValidationMessage.EMAIL_ALREADY_EXISTS.getFormattedMessage(request.getEmail())
            );
        }
    }

}
