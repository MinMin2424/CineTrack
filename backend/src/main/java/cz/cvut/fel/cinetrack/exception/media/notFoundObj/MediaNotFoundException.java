package cz.cvut.fel.cinetrack.exception.media.notFoundObj;

import cz.cvut.fel.cinetrack.exception.NotFoundException;

public class MediaNotFoundException extends NotFoundException {
    public MediaNotFoundException(String message) {
        super(message);
    }
}
