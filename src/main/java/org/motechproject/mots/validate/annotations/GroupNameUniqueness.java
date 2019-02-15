package org.motechproject.mots.validate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.constraintvalidators.GroupNameUniqunessValidator;

@Target(ElementType.TYPE)
@Constraint(validatedBy = {GroupNameUniqunessValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupNameUniqueness {

  /**
   * Specify the message in case of a validation error
   *
   * @return the message about the error
   */
  String message() default ValidationMessages.NOT_UNIQUE_GROUP_NAME;

  /**
   * Specify validation groups, to which this constraint belongs
   *
   * @return array with group classes
   */
  Class<?>[] groups() default {
  };

  /**
   * Specify custom payload objects
   *
   * @return array with payload classes
   */
  Class<? extends Payload>[] payload() default {
  };
}
