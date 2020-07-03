package org.motechproject.mots.validate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.constraintvalidators.SectorExistenceValidator;

@Target(ElementType.FIELD)
@Constraint(validatedBy = {SectorExistenceValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface SectorExistence {

  /**
   * Specify the message in case of a validation error.
   *
   * @return the message about the error
   */
  String message() default ValidationMessageConstants.NOT_EXISTING_SECTOR;

  /**
   * Specify validation groups, to which this constraint belongs.
   *
   * @return array with group classes
   */
  Class<?>[] groups() default {
  };

  /**
   * Specify custom payload objects.
   *
   * @return array with payload classes
   */
  Class<? extends Payload>[] payload() default {
  };
}
