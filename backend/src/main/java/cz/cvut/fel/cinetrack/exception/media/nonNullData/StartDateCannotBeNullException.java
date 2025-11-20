package cz.cvut.fel.cinetrack.exception.media.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class StartDateCannotBeNullException extends CannotBeNullException {
    public StartDateCannotBeNullException(String message) {
        super(message);
    }
}
