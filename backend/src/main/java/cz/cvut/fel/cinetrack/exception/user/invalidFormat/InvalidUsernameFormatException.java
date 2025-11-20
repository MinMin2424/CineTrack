package cz.cvut.fel.cinetrack.exception.user.invalidFormat;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidUsernameFormatException extends InvalidFormatException {
    public InvalidUsernameFormatException(String message) {
        super(message);
    }
}
