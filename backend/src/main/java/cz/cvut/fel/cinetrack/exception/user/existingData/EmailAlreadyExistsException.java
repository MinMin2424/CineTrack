package cz.cvut.fel.cinetrack.exception.user.existingData;

import cz.cvut.fel.cinetrack.exception.AlreadyExistsException;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
