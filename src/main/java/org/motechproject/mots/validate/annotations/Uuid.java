package org.motechproject.mots.validate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import org.motechproject.mots.constants.MotsConstants;

@Target(ElementType.FIELD)
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp= MotsConstants.UUID_PATTERN)
public @interface Uuid {
  String message() default "{invalid.uuid}";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}