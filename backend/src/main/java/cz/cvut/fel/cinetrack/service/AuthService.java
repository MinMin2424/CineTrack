/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.LoginRequest;
import cz.cvut.fel.cinetrack.dto.RegisterRequest;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.InvalidCredentialException;
import cz.cvut.fel.cinetrack.exception.UserNotFoundException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.UsernameAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import cz.cvut.fel.cinetrack.security.AuthenticationResponse;
import cz.cvut.fel.cinetrack.security.JwtService;
import cz.cvut.fel.cinetrack.utils.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private JwtService jwtService;
    private final AvatarService avatarService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AvatarService avatarService, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.avatarService = avatarService;
        this.userValidator = userValidator;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        userValidator.validateRegisterRequest(registerRequest);
        validateUniqueness(registerRequest);

        User user = createUserFromRequest(registerRequest);
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        userValidator.validateLoginRequest(loginRequest);
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        ValidationMessage.USER_NOT_FOUND.getFormattedMessage(loginRequest.getEmail())
                ));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException(ValidationMessage.INVALID_CREDENTIALS.getMessage());
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    private void validateUniqueness(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    ValidationMessage.USERNAME_ALREADY_EXISTS.getFormattedMessage(registerRequest.getUsername())
            );
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(
                    ValidationMessage.EMAIL_ALREADY_EXISTS.getFormattedMessage(registerRequest.getEmail())
            );
        }
    }

    private User createUserFromRequest(RegisterRequest registerRequest) {
        String normalizedEmail = registerRequest.getEmail().toLowerCase().trim();
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setEmail(normalizedEmail);
        user.setAvatar(avatarService.getRandomAvatar());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return user;
    }
}
