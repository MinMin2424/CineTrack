package cz.cvut.fel.cinetrack.exception;

public class PasswordNotStrongEnoughException extends RuntimeException {
    public PasswordNotStrongEnoughException(String message) {
        super(message);
    }
}
