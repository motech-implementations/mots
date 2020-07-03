package org.motechproject.mots.exception;

public class MotsAccessDeniedException extends MotsException {

  private static final long serialVersionUID = 1L;

  public MotsAccessDeniedException(String message) {
    super(message);
  }

  public MotsAccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

}
