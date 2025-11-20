package cz.cvut.fel.cinetrack.exception.user;

import cz.cvut.fel.cinetrack.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
