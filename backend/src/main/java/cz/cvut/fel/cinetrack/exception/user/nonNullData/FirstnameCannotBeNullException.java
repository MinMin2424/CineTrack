package cz.cvut.fel.cinetrack.exception.user.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class FirstnameCannotBeNullException extends CannotBeNullException {
    public FirstnameCannotBeNullException(String message) {
        super(message);
    }
}
