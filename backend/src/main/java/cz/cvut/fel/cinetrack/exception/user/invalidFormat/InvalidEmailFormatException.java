package cz.cvut.fel.cinetrack.exception.user.invalidFormat;

import cz.cvut.fel.cinetrack.exception.InvalidFormatException;

public class InvalidEmailFormatException extends InvalidFormatException {
  public InvalidEmailFormatException(String message) {
    super(message);
  }
}
