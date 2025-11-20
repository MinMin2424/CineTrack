package cz.cvut.fel.cinetrack.exception.media.existingData;

import cz.cvut.fel.cinetrack.exception.AlreadyExistsException;

public class SeriesAlreadyExistsException extends AlreadyExistsException {
    public SeriesAlreadyExistsException(String message) {
        super(message);
    }
}
