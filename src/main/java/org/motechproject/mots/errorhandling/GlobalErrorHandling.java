package org.motechproject.mots.errorhandling;

import java.util.Map;
import org.motechproject.mots.exception.BindingResultException;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.exception.MotsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalErrorHandling extends AbstractErrorHandling {

  @ExceptionHandler(MotsException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleMotsException(MotsException ex) {
    return logAndGetErrorResponse(ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleException(Exception ex) {
    String message = "Unexpected error occurred, check the log for more details";
    return logAndGetErrorResponse(message, ex);
  }

  @ExceptionHandler(BindingResultException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Map<String, String> handleBindingResultException(BindingResultException ex) {
    return ex.getErrors();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleIllegalArgumentException(IllegalArgumentException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public ErrorResponse entityNotFoundException(EntityNotFoundException ex) {
    return logAndGetErrorResponse(ex);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public String handleAccessDeniedException(Exception ex, WebRequest request) {
    return ex.getMessage();
  }
}
