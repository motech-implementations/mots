package org.motechproject.mots.exception;

public class IvrException extends Exception {

  private static final long serialVersionUID = 1L;

  public IvrException(String message) {
    super(message);
  }

  public IvrException(String message, Throwable cause) {
    super(message, cause);
  }
}
