package cz.cvut.fel.cinetrack.exception.user.existingData;

import cz.cvut.fel.cinetrack.exception.AlreadyExistsException;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
  public UsernameAlreadyExistsException(String message) {
    super(message);
  }
}
