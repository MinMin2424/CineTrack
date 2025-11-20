/*
 * Created by minmin_tranova on 05.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserAvatarRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.ChangeUserPasswordRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.request.EditUserProfileRequestDTO;
import cz.cvut.fel.cinetrack.dto.user.response.CombinedUserProfileResponseDTO;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileHeaderResponseDTO;
import cz.cvut.fel.cinetrack.dto.user.response.UserProfileResponseDTO;
import cz.cvut.fel.cinetrack.exception.user.UserNotFoundException;
import cz.cvut.fel.cinetrack.exception.user.existingData.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.user.existingData.UsernameAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.validator.UserValidator;
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
    public UserProfileResponseDTO getCurrentUserProfile() {
        User currentUser = getCurrentUserNotDeleted();
        return new UserProfileResponseDTO(
                currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getEmail()
        );
    }

    @Transactional
    public UserProfileHeaderResponseDTO getCurrentUserProfileHeader() {
        User currentUser = getCurrentUserNotDeleted();
        return new UserProfileHeaderResponseDTO(
                "@" + currentUser.getUsername(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getCreationDate(),
                currentUser.getAvatar()
        );
    }

    @Transactional
    public CombinedUserProfileResponseDTO getCombinedUserProfile() {
        return new CombinedUserProfileResponseDTO(
                getCurrentUserProfile(),
                getCurrentUserProfileHeader()
        );
    }

    @Transactional
    public UserProfileResponseDTO editUserProfile(EditUserProfileRequestDTO request) {
        User currentUser = getCurrentUserNotDeleted();
        userValidator.validateEditProfileData(request);
        validateUniqueness(request, currentUser.getId());

        currentUser.setUsername(request.getUsername());
        currentUser.setFirstname(request.getFirstname());
        currentUser.setLastname(request.getLastname());
        currentUser.setEmail(request.getEmail());
        currentUser.setLastModified(LocalDateTime.now());

        User updatedUser = userRepository.save(currentUser);
        return new UserProfileResponseDTO(
                updatedUser.getUsername(),
                updatedUser.getFirstname(),
                updatedUser.getLastname(),
                updatedUser.getEmail()
        );
    }

    @Transactional
    public void changeUserPassword(ChangeUserPasswordRequestDTO request) {
        User currentUser = getCurrentUserNotDeleted();
        userValidator.validateUserPassword(request);
        currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        currentUser.setLastModified(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    @Transactional
    public void changeUserAvatar(ChangeUserAvatarRequestDTO request) {
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

    private void validateUniqueness(EditUserProfileRequestDTO request, Long currentUserId) {
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
