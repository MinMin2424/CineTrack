package cz.cvut.fel.cinetrack.exception.media.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class MediaInputCannotBeNullException extends CannotBeNullException {
    public MediaInputCannotBeNullException(String message) {
        super(message);
    }
}
