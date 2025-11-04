/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.LoginRequest;
import cz.cvut.fel.cinetrack.dto.RegisterRequest;
import cz.cvut.fel.cinetrack.exception.PasswordNotStrongEnoughException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.InvalidCredentialException;
import cz.cvut.fel.cinetrack.exception.UserNotFoundException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.UsernameAlreadyExistsException;
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
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import cz.cvut.fel.cinetrack.security.AuthenticationResponse;
import cz.cvut.fel.cinetrack.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AvatarService avatarService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        validRegisterRequest = new RegisterRequest(
                "testUser",
                "Mina",
                "Tranová",
                "mina.tranova@test.cz",
                "Test01"
        );

        validLoginRequest = new LoginRequest(
                "mina.tranova@test.cz",
                "Test01"
        );
    }

    /* ******************* REGISTER ******************* */

    @Test
    void register_withValidData_shouldCreateUserAndReturnToken() {
        AuthenticationResponse response = authService.register(validRegisterRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertTrue(jwtService.isTokenValid(response.getToken()));

        Optional<User> savedUser = userRepository.findByEmail("mina.tranova@test.cz");

        assertTrue(savedUser.isPresent());
        assertEquals("testUser", savedUser.get().getUsername());
        assertEquals("Mina", savedUser.get().getFirstname());
        assertEquals("Tranová", savedUser.get().getLastname());
        assertTrue(passwordEncoder.matches("Test01", savedUser.get().getPassword()));
        assertNotNull(savedUser.get().getAvatar());
    }

    @Test
    void register_withDuplicateUsername_shouldThrowException() {
        authService.register(validRegisterRequest);

        RegisterRequest duplicateRequest = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "test@test.cz",
                "Test01"
        );

        UsernameAlreadyExistsException e = assertThrows(
                UsernameAlreadyExistsException.class,
                () -> authService.register(duplicateRequest)
        );
        assertEquals("User with this username: testUser already exists!", e.getMessage());
    }

    @Test
    void register_withDuplicateEmail_shouldThrowException() {
        authService.register(validRegisterRequest);

        RegisterRequest duplicateRequest = new RegisterRequest(
                "testUser2",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );

        EmailAlreadyExistsException e = assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(duplicateRequest)
        );
        assertEquals("User with this email: mina.tranova@test.cz already exists!", e.getMessage());
    }

    @Test
    void register_withInvalidUsernameLength_lessThan5_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "test ",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidUsernameLengthException e = assertThrows(
                InvalidUsernameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("Username must be between 5 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withInvalidUsernameLength_moreThan15_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "test567891234564 ",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidUsernameLengthException e = assertThrows(
                InvalidUsernameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("Username must be between 5 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withInvalidFirstnameLength_lessThan3_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "I ",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidFirstnameLengthException e = assertThrows(
                InvalidFirstnameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("First name must be between 3 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withInvalidFirstnameLength_moreThan15_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "AhojJsemMinaTranova ",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidFirstnameLengthException e = assertThrows(
                InvalidFirstnameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("First name must be between 3 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withInvalidLastnameLength_lessThan3_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "Mina",
                "Sm ",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidLastnameLengthException e = assertThrows(
                InvalidLastnameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("Last name must be between 3 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withInvalidLastnameLength_moreThan15_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "Mina",
                " AhojJsemMinaTranova ",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidLastnameLengthException e = assertThrows(
                InvalidLastnameLengthException.class,
                () -> authService.register(request)
        );
        assertEquals("Last name must be between 3 and 15 characters!", e.getMessage());
    }

    @Test
    void register_withMissingUsername_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                null,
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        UsernameCannotBeNullException e = assertThrows(
                UsernameCannotBeNullException.class,
                () -> authService.register(request)
        );
        assertEquals("Username is required!", e.getMessage());
    }

    @Test
    void register_withMissingFirstname_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                null,
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        FirstnameCannotBeNullException e = assertThrows(
                FirstnameCannotBeNullException.class,
                () -> authService.register(request)
        );
        assertEquals("First name is required!", e.getMessage());
    }

    @Test
    void register_withMissingLastname_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                null,
                "mina.tranova@test.cz",
                "Test01"
        );
        LastnameCannotBeNullException e = assertThrows(
                LastnameCannotBeNullException.class,
                () -> authService.register(request)
        );
        assertEquals("Last name is required!", e.getMessage());
    }

    @Test
    void register_withMissingEmail_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                null,
                "Test01"
        );
        EmailCannotBeNullException e = assertThrows(
                EmailCannotBeNullException.class,
                () -> authService.register(request)
        );
        assertEquals("Email is required!", e.getMessage());
    }

    @Test
    void register_withMissingPassword_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                null
        );
        PasswordCannotBeNullException e = assertThrows(
                PasswordCannotBeNullException.class,
                () -> authService.register(request)
        );
        assertEquals("Password is required!", e.getMessage());
    }

    @Test
    void register_withInvalidUsernameFormat_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "test=2024?test-",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test01"
        );
        InvalidUsernameFormatException e = assertThrows(
                InvalidUsernameFormatException.class,
                () -> authService.register(request)
        );
        assertEquals("Username can only contain letters, numbers, and underscores!", e.getMessage());
    }

    @Test
    void register_withInvalidEmailFormat_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova.test.cz",
                "Test01"
        );
        InvalidEmailFormatException e = assertThrows(
                InvalidEmailFormatException.class,
                () -> authService.register(request)
        );
        assertEquals("Email should be valid!", e.getMessage());
    }

    @Test
    void register_withPasswordLengthLessThan6_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Test1"
        );
        PasswordNotStrongEnoughException e = assertThrows(
                PasswordNotStrongEnoughException.class,
                () -> authService.register(request)
        );
        assertEquals("Password must be at least 6 characters!", e.getMessage());
    }

    @Test
    void register_withPasswordWithoutNumber_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "Testtest"
        );
        PasswordNotStrongEnoughException e = assertThrows(
                PasswordNotStrongEnoughException.class,
                () -> authService.register(request)
        );
        assertEquals("Password is too weak. Must contain at least one uppercase letter, one lowercase letter, and one number!", e.getMessage());
    }

    @Test
    void register_withPasswordWithoutUppercaseLetter_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "test01"
        );
        PasswordNotStrongEnoughException e = assertThrows(
                PasswordNotStrongEnoughException.class,
                () -> authService.register(request)
        );
        assertEquals("Password is too weak. Must contain at least one uppercase letter, one lowercase letter, and one number!", e.getMessage());
    }

    @Test
    void register_withPasswordWithoutLowercaseLetter_shouldThrowException() {
        RegisterRequest request = new RegisterRequest(
                "testUser",
                "John",
                "Smith",
                "mina.tranova@test.cz",
                "TEST01"
        );
        PasswordNotStrongEnoughException e = assertThrows(
                PasswordNotStrongEnoughException.class,
                () -> authService.register(request)
        );
        assertEquals("Password is too weak. Must contain at least one uppercase letter, one lowercase letter, and one number!", e.getMessage());
    }

    /* ******************* LOGIN ******************* */

    @Test
    void login_withValidCredentials_shouldReturnTokenAndUpdateLastLogin() {
        authService.register(validRegisterRequest);
        LocalDateTime beforeLogin = LocalDateTime.now();

        AuthenticationResponse response = authService.login(validLoginRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertTrue(jwtService.isTokenValid(response.getToken()));

        User user = userRepository.findByEmail("mina.tranova@test.cz")
                .orElseThrow();
        assertNotNull(user.getLastLogin());
        assertTrue(user.getLastLogin().isAfter(beforeLogin) | user.getLastLogin().isEqual(beforeLogin));
    }

    @Test
    void login_withNullOrEmptyEmail_shouldThrowException() {
        LoginRequest loginRequest = new LoginRequest(
                " ",
                "Test01"
        );
        EmailCannotBeNullException e = assertThrows(
                EmailCannotBeNullException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals("Email is required!", e.getMessage());
    }

    @Test
    void login_withNullOrEmptyPassword_shouldThrowException() {
        LoginRequest loginRequest = new LoginRequest(
                "mina.tranova@test.cz",
                " "
        );
        PasswordCannotBeNullException e = assertThrows(
                PasswordCannotBeNullException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals("Password is required!", e.getMessage());
    }

    @Test
    void login_withNonExistenceEmail_shouldThrowException() {
        LoginRequest loginRequest = new LoginRequest(
                "mina.tranova@test.cz",
                "Test01"
        );
        UserNotFoundException e = assertThrows(
                UserNotFoundException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals("User with this email: mina.tranova@test.cz not found!", e.getMessage());
    }

    @Test
    void login_withWrongPassword_shouldThrowException() {
        authService.register(validRegisterRequest);
        LoginRequest loginRequest = new LoginRequest(
                "mina.tranova@test.cz",
                "Test02"
        );
        InvalidCredentialException e = assertThrows(
                InvalidCredentialException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals("Invalid credentials!", e.getMessage());
    }

    @Test
    void login_withCorrectPasswordButDifferentCase_shouldThrowException() {
        authService.register(validRegisterRequest);
        LoginRequest loginRequest = new LoginRequest(
                "mina.tranova@test.cz",
                "TEST02"
        );
        InvalidCredentialException e = assertThrows(
                InvalidCredentialException.class,
                () -> authService.login(loginRequest)
        );
        assertEquals("Invalid credentials!", e.getMessage());
    }


}
