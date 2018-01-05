package org.motechproject.mots.exception;

public class IvrException extends Exception {
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

  public String getClearVotoInfo() {
    return this.clearVotoInfo;
  }
}
