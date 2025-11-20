package cz.cvut.fel.cinetrack.exception.user.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class PasswordCannotBeNullException extends CannotBeNullException {
    public PasswordCannotBeNullException(String message) {
        super(message);
    }
}
