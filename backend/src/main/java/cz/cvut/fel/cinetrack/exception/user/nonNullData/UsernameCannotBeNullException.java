package cz.cvut.fel.cinetrack.exception.user.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class UsernameCannotBeNullException extends CannotBeNullException {
    public UsernameCannotBeNullException(String message) {
        super(message);
    }
}
