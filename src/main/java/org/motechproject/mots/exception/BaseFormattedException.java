package org.motechproject.mots.exception;

import java.text.MessageFormat;

public abstract class BaseFormattedException extends MotsException {
  public BaseFormattedException(String message) {
    super(message);
  }

  public BaseFormattedException(Throwable cause, String message) {
    super(message, cause);
  }

  public BaseFormattedException(String format, final Object... parameters) {
    super(MessageFormat.format(format, parameters));
  }

  public BaseFormattedException(Throwable cause, String format, final Object... parameters) {
    super(MessageFormat.format(format, parameters), cause);
  }
}
