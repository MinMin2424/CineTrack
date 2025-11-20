package cz.cvut.fel.cinetrack.exception.media;

public class RequestFailedException extends RuntimeException {
    public RequestFailedException(String message) {
        super(message);
    }
}
