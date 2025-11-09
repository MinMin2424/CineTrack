/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.exception;

import cz.cvut.fel.cinetrack.exception.existingData.AlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.existingData.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.existingData.UsernameAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidFormatException;
import cz.cvut.fel.cinetrack.exception.nonNullData.AvatarCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.CannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.EmailCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.FirstnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.LastnameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.PasswordCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.nonNullData.UsernameCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidEmailFormatException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidFirstnameLengthException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidLastnameLengthException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidUsernameFormatException;
import cz.cvut.fel.cinetrack.exception.invalidFormat.InvalidUsernameLengthException;
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

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExistsException(
            AlreadyExistsException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(
            InvalidFormatException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotBeNullException.class)
    public ResponseEntity<Map<String, String>> handleCannotBeNullException(
            CannotBeNullException ex
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
