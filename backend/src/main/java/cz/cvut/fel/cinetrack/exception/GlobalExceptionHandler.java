/*
 * Created by minmin_tranova on 04.11.2025
 */

package cz.cvut.fel.cinetrack.exception;

import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.EmailAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(
            UserNotFoundException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialException(
            InvalidCredentialException ex
    ) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
