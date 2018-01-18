package org.motechproject.mots.exception;

import java.util.Map;
import lombok.Getter;

public class BindingResultException extends RuntimeException {

  @Getter
  private final Map<String, String> errors;

  public BindingResultException(Map<String, String> errors) {
    this.errors = errors;
  }
}
