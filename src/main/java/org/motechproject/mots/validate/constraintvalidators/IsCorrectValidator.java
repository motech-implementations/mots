package org.motechproject.mots.validate.constraintvalidators;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.domain.Choice;
import org.motechproject.mots.domain.enums.ChoiceType;
import org.motechproject.mots.validate.annotations.IsCorrect;

public class IsCorrectValidator
    implements ConstraintValidator<IsCorrect, List<Choice>> {

  @Override
  public void initialize(IsCorrect constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<Choice> values, ConstraintValidatorContext context) {
    int correct = 0;
    for (Choice choice : values) {
      if (ChoiceType.CORRECT.equals(choice.getType())) {
        correct++;
      }
    }

    return correct == 1;
  }
}
