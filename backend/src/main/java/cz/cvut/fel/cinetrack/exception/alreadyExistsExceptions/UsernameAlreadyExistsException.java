package cz.cvut.fel.cinetrack.exception.alreadyExistsExceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
