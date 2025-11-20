package cz.cvut.fel.cinetrack.exception.media.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class DatesCannotBeNullException extends CannotBeNullException {
    public DatesCannotBeNullException(String message) {
        super(message);
    }
}
