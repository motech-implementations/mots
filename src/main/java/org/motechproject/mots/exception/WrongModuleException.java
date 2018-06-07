package org.motechproject.mots.exception;

public class WrongModuleException extends CourseProgressException {

  public WrongModuleException(String message) {
    super(message);
  }

  public WrongModuleException(String format, Object... parameters) {
    super(format, parameters);
  }
}
