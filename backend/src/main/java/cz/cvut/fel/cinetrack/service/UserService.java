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
import cz.cvut.fel.cinetrack.exception.UserNotFoundException;
import cz.cvut.fel.cinetrack.exception.existingData.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.existingData.UsernameAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserProfileResponse getCurrentUserProfile() {
        User currentUser = getCurrentUserNotDeleted();
        return new UserProfileResponse(
                currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getEmail()
        );
    }

    @Transactional
    public UserProfileHeaderResponse getCurrentUserProfileHeader() {
        User currentUser = getCurrentUserNotDeleted();
        return new UserProfileHeaderResponse(
                "@" + currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getCreationDate(),
                currentUser.getAvatar()
        );
    }

    @Transactional
    public CombinedUserProfile getCombinedUserProfile() {
        return new CombinedUserProfile(
                getCurrentUserProfile(),
                getCurrentUserProfileHeader()
        );
    }

    @Transactional
    public UserProfileResponse editUserProfile(EditUserProfileRequest request) {
        User currentUser = getCurrentUserNotDeleted();
        userValidator.validateEditProfileData(request);
        validateUniqueness(request, currentUser.getId());

        currentUser.setUsername(request.getUsername());
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setLastModified(LocalDateTime.now());

        User updatedUser = userRepository.save(currentUser);
        return new UserProfileResponse(
                updatedUser.getUsername(),
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail()
        );
    }

    @Transactional
    public void changeUserPassword(ChangeUserPasswordRequest request) {
        User currentUser = getCurrentUserNotDeleted();
        userValidator.validateUserPassword(request);
        currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        currentUser.setLastModified(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    @Transactional
    public void changeUserAvatar(ChangeUserAvatarRequest request) {
        User currentUser = getCurrentUserNotDeleted();
        userValidator.validateUserAvatar(request);
        currentUser.setAvatar(request.getAvatar());
        currentUser.setLastModified(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteCurrentUser() {
        User currentUser = getCurrentUserNotDeleted();
        currentUser.setDeleted(true);
        currentUser.setDeletionDate(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    private void validateUniqueness(EditUserProfileRequest request, Long currentUserId) {
        Optional<User> existingUserByUsername = userRepository.findByUsername(request.getUsername());
        if (existingUserByUsername.isPresent() && !existingUserByUsername.get().getId().equals(currentUserId)) {
            if (!existingUserByUsername.get().isDeleted()) {
                throw new UsernameAlreadyExistsException(
                        ValidationMessage.USERNAME_ALREADY_EXISTS.getFormattedMessage(request.getUsername())
                );
            }
        }
        Optional<User> existingUserByEmail = userRepository.findByEmail(request.getEmail());
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(currentUserId)) {
            if (!existingUserByEmail.get().isDeleted()) {
                throw new EmailAlreadyExistsException(
                        ValidationMessage.EMAIL_ALREADY_EXISTS.getFormattedMessage(request.getEmail())
                );
            }
        }
    }

    private User getCurrentUserNotDeleted() {
        User currentUser = SecurityUtils.getCurrentUser();
        return userRepository.findByEmailAndNotDeleted(currentUser.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found or account is deleted!"));
    }

}
