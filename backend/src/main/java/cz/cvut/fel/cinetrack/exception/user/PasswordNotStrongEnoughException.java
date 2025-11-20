package cz.cvut.fel.cinetrack.exception.user;

public class PasswordNotStrongEnoughException extends RuntimeException {
    public PasswordNotStrongEnoughException(String message) {
        super(message);
    }
}
