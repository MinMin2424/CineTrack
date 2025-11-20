package cz.cvut.fel.cinetrack.exception.media;

public class SeriesNotFoundException extends RuntimeException {
    public SeriesNotFoundException(String message) {
        super(message);
    }
}
