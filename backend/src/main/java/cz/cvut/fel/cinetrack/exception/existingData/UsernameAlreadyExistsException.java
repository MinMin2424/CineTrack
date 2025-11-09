package cz.cvut.fel.cinetrack.exception.existingData;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
