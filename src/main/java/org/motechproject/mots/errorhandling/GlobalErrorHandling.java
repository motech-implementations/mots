package org.motechproject.mots.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalErrorHandling extends AbstractErrorHandling {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleException(Exception ex) {
    String message = "Unexpected error occurred, check the log for more details";
    return logAndGetErrorResponse(message, ex);
  }
}
