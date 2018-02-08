package org.motechproject.mots.validate.annotations.incharge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@InchargeDtoFacilityUniqueness
@InchargeDtoPhoneNumberUniqueness
public @interface InchargeDtoDbConstraints {

  /**
   * Specify the message in case of a validation error
   *
   * @return the message about the error
   */
  String message() default "invalid.incharge.db_constraints";

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
