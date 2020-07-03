package org.motechproject.mots.exception;

public class CourseProgressException extends BaseFormattedException {

  private static final long serialVersionUID = 1L;

  public CourseProgressException(String message) {
    super(message);
  }

  public CourseProgressException(String format, Object... parameters) {
    super(format, parameters);
  }
}
