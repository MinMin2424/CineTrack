package cz.cvut.fel.cinetrack.exception.media.notFoundObj;

import cz.cvut.fel.cinetrack.exception.NotFoundException;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(String message) {
        super(message);
    }
}
