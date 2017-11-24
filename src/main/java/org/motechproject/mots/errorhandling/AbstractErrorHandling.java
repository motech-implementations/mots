package org.motechproject.mots.errorhandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class AbstractErrorHandling {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractErrorHandling.class);

  protected ErrorResponse logAndGetErrorResponse(String message, Exception ex) {
    LOGGER.error(message, ex);
    return new ErrorResponse(message, ex);
  }
}
