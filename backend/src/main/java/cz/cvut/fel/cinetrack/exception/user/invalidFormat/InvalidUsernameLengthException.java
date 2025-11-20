package cz.cvut.fel.cinetrack.exception.user.invalidFormat;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidUsernameLengthException extends InvalidFormatException {
    public InvalidUsernameLengthException(String message) {
        super(message);
    }
}
