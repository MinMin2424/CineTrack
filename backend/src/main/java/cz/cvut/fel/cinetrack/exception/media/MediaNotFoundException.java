package cz.cvut.fel.cinetrack.exception.media;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(String message) {
        super(message);
    }
}
