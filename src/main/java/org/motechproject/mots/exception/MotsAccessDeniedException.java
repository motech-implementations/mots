package org.motechproject.mots.exception;

public class MotsAccessDeniedException extends MotsException {

  public MotsAccessDeniedException(String message) {
    super(message);
  }

  public MotsAccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

}
