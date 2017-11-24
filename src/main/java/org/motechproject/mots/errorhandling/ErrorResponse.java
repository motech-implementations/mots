package org.motechproject.mots.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;

@AllArgsConstructor
public class ErrorResponse {

  @Getter
  private String message;

  @Getter
  private String stacktrace;

  public ErrorResponse(String message, Exception ex) {
    this.message = message;
    this.stacktrace = ExceptionUtils.getStackTrace(ex);
  }
}
