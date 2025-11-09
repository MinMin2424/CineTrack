/*
 * Created by minmin_tranova on 09.11.2025
 */

package cz.cvut.fel.cinetrack.exception.invalidFormat;

public abstract class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }
}
