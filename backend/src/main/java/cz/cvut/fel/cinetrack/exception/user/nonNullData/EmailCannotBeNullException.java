package cz.cvut.fel.cinetrack.exception.user.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class EmailCannotBeNullException extends CannotBeNullException {
    public EmailCannotBeNullException(String message) {
        super(message);
    }
}
