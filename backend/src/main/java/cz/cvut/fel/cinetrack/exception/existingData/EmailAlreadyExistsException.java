package cz.cvut.fel.cinetrack.exception.existingData;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
