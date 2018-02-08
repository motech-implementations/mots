package org.motechproject.mots.validate;

import java.util.regex.Pattern;

public final class ValidationUtils {

  public static boolean matchPattern(String stringPattern, String value) {
    Pattern pattern = Pattern.compile(stringPattern);
    return pattern.matcher(value).matches();
  }

  private ValidationUtils() {
  }
}
