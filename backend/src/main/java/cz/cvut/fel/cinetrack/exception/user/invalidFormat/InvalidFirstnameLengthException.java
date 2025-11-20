package cz.cvut.fel.cinetrack.exception.user.invalidFormat;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidFirstnameLengthException extends InvalidFormatException {
    public InvalidFirstnameLengthException(String message) {
        super(message);
    }
}
