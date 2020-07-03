package org.motechproject.mots.exception;

import lombok.Getter;

public class IvrException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter
  private String clearVotoInfo = "";

  public IvrException(String message) {
    super(message);
  }

  public IvrException(String message, Throwable cause) {
    super(message, cause);
  }

  public IvrException(String message, Throwable cause, String clearVotoInfo) {
    super(message, cause);
    this.clearVotoInfo = clearVotoInfo;
  }
}
