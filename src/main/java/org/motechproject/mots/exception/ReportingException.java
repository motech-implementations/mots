package org.motechproject.mots.exception;

public class ReportingException extends BaseFormattedException {

  private static final long serialVersionUID = 1L;

  public ReportingException(String message) {
    super(message);
  }

  public ReportingException(String format, Object... parameters) {
    super(format, parameters);
  }

  public ReportingException(Throwable cause, String format, Object... parameters) {
    super(cause, format, parameters);
  }
}
