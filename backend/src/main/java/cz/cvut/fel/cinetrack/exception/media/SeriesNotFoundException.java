package cz.cvut.fel.cinetrack.exception.media;

import cz.cvut.fel.cinetrack.exception.NotFoundException;

public class SeriesNotFoundException extends NotFoundException {
    public SeriesNotFoundException(String message) {
        super(message);
    }
}
