/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.exception;

import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.UsernameAlreadyExistsException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildErrorResponse(
            String message,
            HttpStatus status
    ) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("message", message);
        return ResponseEntity.status(status).body(errorBody);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AvatarCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleAvatarCannotBeNullException(
            AvatarCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleEmailCannotBeNullException(
            EmailCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FirstnameCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleFirstnameCannotBeNullException(
            FirstnameCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LastnameCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleLastnameCannotBeNullException(
            LastnameCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handlePasswordCannotBeNullException(
            PasswordCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameCannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleUsernameCannotBeNullException(
            UsernameCannotBeNullException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmailFormatException(
            InvalidEmailFormatException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFirstnameLengthException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFirstnameLengthException(
            InvalidFirstnameLengthException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidLastnameLengthException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLastnameLengthException(
            InvalidLastnameLengthException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUsernameFormatException(
            InvalidUsernameFormatException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUsernameLengthException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUsernameLengthException(
            InvalidUsernameLengthException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialException(
            InvalidCredentialException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordNotStrongEnoughException.class)
    public ResponseEntity<Map<String, String>> handlePasswordNotStrongEnoughException(
            PasswordNotStrongEnoughException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationCredentialsNotFoundException(
            AuthenticationCredentialsNotFoundException ex
    ) {
        return buildErrorResponse("You have to be logged in to access this endpoint", HttpStatus.UNAUTHORIZED);
    }
}
