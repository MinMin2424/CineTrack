package cz.cvut.fel.cinetrack.exception.media.existingData;

import cz.cvut.fel.cinetrack.exception.AlreadyExistsException;

public class MovieAlreadyExistsException extends AlreadyExistsException {
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}
