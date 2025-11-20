package cz.cvut.fel.cinetrack.exception.user.nonNullData;

import cz.cvut.fel.cinetrack.exception.CannotBeNullException;

public class AvatarCannotBeNullException extends CannotBeNullException {
    public AvatarCannotBeNullException(String message) {
        super(message);
    }
}
