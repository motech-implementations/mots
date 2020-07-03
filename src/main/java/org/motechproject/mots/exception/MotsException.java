package org.motechproject.mots.exception;

import lombok.Getter;

public class MotsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Getter
  private String displayMessage;

  @Getter
  private String details;

  /**
   * Creates new MOTS exception with display message.
   * @param displayMessage message that will be displayed to the user
   */
  public MotsException(String displayMessage) {
    super(displayMessage);
    this.displayMessage = displayMessage;
  }

  /**
   * Creates new MOTS exception with display message and cause.
   * @param displayMessage message that will be displayed to the user
   * @param cause cause of this exception
   */
  public MotsException(String displayMessage, Throwable cause) {
    super(getMessage(displayMessage, cause), cause);

    this.displayMessage = displayMessage;
    this.details = getCauseMessage(cause);
  }

  private static String getMessage(String displayMessage, Throwable cause) {
    return displayMessage + "\n" + getCauseMessage(cause);
  }

  private static String getCauseMessage(Throwable cause) {
    return "Caused by: " + cause.getMessage();
  }
}
