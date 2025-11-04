package cz.cvut.fel.cinetrack.exception.invalidFormatExceptions;

public class InvalidUsernameLengthException extends RuntimeException {
    public InvalidUsernameLengthException(String message) {
        super(message);
    }
}
