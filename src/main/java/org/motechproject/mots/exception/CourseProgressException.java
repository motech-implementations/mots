package org.motechproject.mots.exception;

public class CourseProgressException extends BaseFormattedException {

  public CourseProgressException(String message) {
    super(message);
  }

  public CourseProgressException(String format, Object... parameters) {
    super(format, parameters);
  }
}
