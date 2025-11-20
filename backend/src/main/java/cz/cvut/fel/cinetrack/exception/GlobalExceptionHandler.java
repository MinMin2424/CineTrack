/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.exception;

import cz.cvut.fel.cinetrack.exception.media.MediaNotFoundException;
import cz.cvut.fel.cinetrack.exception.media.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.exception.user.PasswordNotStrongEnoughException;
import cz.cvut.fel.cinetrack.exception.user.UserNotFoundException;
import org.apache.coyote.Response;
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

    // AUTH

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

    // MEDIA
    @ExceptionHandler(MediaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMediaNotFoundException(
            MediaNotFoundException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeriesNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSeriesNotFoundException(
            SeriesNotFoundException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
