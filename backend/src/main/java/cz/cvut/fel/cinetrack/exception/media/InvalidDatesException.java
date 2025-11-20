package cz.cvut.fel.cinetrack.exception.media;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidDatesException extends InvalidFormatException {
    public InvalidDatesException(String message) {
        super(message);
    }
}
