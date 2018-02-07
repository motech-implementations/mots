package org.motechproject.mots.validate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.motechproject.mots.validate.constraintvalidators.UuidValidator;

@Target(ElementType.FIELD)
@Constraint(validatedBy={UuidValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface Uuid {
  String message() default "{invalid.uuid}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}