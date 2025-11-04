package cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
