package org.motechproject.mots.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.motechproject.mots.exception.MotsException;

@AllArgsConstructor
public class ErrorResponse {

  @Getter
  private String message;

  @Getter
  private String details;

  @Getter
  private String stacktrace;

  /**
   * Creates new error response with message and stacktrace.
   * @param message message displayed to the user
   * @param ex cause of the error
   */
  public ErrorResponse(String message, Exception ex) {
    this.message = message;
    this.stacktrace = ExceptionUtils.getStackTrace(ex);
  }

  /**
   * Creates new error response with message, details and stacktrace.
   * @param ex cause of the error
   */
  public ErrorResponse(MotsException ex) {
    this.message = ex.getDisplayMessage();
    this.details = ex.getDetails();
    this.stacktrace = ExceptionUtils.getStackTrace(ex);
  }
}
