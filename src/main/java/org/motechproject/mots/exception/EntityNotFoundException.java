package org.motechproject.mots.exception;

public class EntityNotFoundException extends BaseFormattedException {

  private static final long serialVersionUID = 1L;

  public EntityNotFoundException(String displayMessage) {
    super(displayMessage);
  }

  public EntityNotFoundException(String format, Object... parameters) {
    super(format, parameters);
  }
}
