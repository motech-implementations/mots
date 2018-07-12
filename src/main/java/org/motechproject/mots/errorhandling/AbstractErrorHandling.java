package org.motechproject.mots.errorhandling;

import org.apache.log4j.Logger;
import org.motechproject.mots.exception.MotsException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AbstractErrorHandling {

  private static final Logger LOGGER = Logger.getLogger(AbstractErrorHandling.class);

  protected ErrorResponse logAndGetErrorResponse(String message, Exception ex) {
    LOGGER.error(message, ex);
    return new ErrorResponse(message, ex);
  }

  protected ErrorResponse logAndGetErrorResponse(MotsException ex) {
    LOGGER.error(ex.getMessage(), ex);
    return new ErrorResponse(ex);
  }
}
