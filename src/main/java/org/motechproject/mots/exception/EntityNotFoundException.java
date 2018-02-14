package org.motechproject.mots.exception;

public class EntityNotFoundException extends BaseFormattedException {

  public EntityNotFoundException(String displayMessage) {
    super(displayMessage);
  }

  public EntityNotFoundException(String format, Object... parameters) {
    super(format, parameters);
  }
}
