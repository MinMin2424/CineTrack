/*
 * Created by minmin_tranova on 09.11.2025
 */

package cz.cvut.fel.cinetrack.exception.existingData;

public abstract class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
