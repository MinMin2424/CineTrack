package cz.cvut.fel.cinetrack.exception.user.invalidFormat;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidLastnameLengthException extends InvalidFormatException {
    public InvalidLastnameLengthException(String message) {
        super(message);
    }
}
