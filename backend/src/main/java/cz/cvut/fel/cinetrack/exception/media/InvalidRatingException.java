package cz.cvut.fel.cinetrack.exception.media;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidRatingException extends InvalidFormatException {
    public InvalidRatingException(String message) {
        super(message);
    }
}
