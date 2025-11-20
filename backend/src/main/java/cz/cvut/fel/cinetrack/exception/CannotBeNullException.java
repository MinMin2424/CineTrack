package cz.cvut.fel.cinetrack.exception;

public abstract class CannotBeNullException extends RuntimeException {
    public CannotBeNullException(String message) {
        super(message);
    }
}
