package org.motechproject.mots.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public final class ValidationUtils {

  private static final String UUID_PATTERN =
      "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

  public static boolean isValidUuidString(String string) {
    return matchPattern(UUID_PATTERN, string);
  }

  /**
   * Validation of uuids.
   *
   * @param values collection of uuids
   * @return true if all strings are valid UUIDs, false otherwise
   */
  public static boolean validateUuids(Collection<String> values) {
    for (String uuid : values) {
      if (!StringUtils.isNotEmpty(uuid) || !ValidationUtils.isValidUuidString(uuid)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Validation of date format.
   *
   * @param format is the format of date
   * @param value is a date value to check
   * @return true if date is valid against given format
   */
  public static boolean isValidDateFormat(String format, String value) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

    try {
      Date date = sdf.parse(value);
      return value.equals(sdf.format(date));
    } catch (ParseException ex) {
      return false;
    }
  }

  public static boolean matchPattern(String stringPattern, String value) {
    Pattern pattern = Pattern.compile(stringPattern);
    return pattern.matcher(value).matches();
  }

  /**
   * Add the default violation message as the error message for a inner field.
   *
   * @param context is the validation context
   * @param fieldName is a name of the field inner the object to which the annotation was applied
   */
  public static void addDefaultViolationMessageToInnerField(ConstraintValidatorContext context,
      String fieldName) {
    addDefaultViolationMessageToInnerField(context, fieldName,
        context.getDefaultConstraintMessageTemplate());
  }

  /**
   * Add the custom violation message as the error message for a inner field.
   *
   * @param context is the validation context
   * @param fieldName is the name of the field inner the object to which the annotation was applied
   * @param message is the violation message that will be reported as the error message
   */
  public static void addDefaultViolationMessageToInnerField(ConstraintValidatorContext context,
      String fieldName, String message) {
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(fieldName)
        .addConstraintViolation();
  }

  /**
   * Add the custom violation message as the error message.
   *
   * @param context is the validation context
   * @param message is the violation message that will be reported as the error message
   */
  public static void addDefaultViolationMessage(
      ConstraintValidatorContext context, String message) {
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}
