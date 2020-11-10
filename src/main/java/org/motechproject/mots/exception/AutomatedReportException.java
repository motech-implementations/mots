package org.motechproject.mots.exception;

public class AutomatedReportException extends MotsException {

  private static final long serialVersionUID = 1L;

  public AutomatedReportException(String message) {
    super(message);
  }

  public AutomatedReportException(String message, Throwable cause) {
    super(message, cause);
  }
}
