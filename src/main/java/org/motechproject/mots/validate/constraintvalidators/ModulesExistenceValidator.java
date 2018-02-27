package org.motechproject.mots.validate.constraintvalidators;

import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.repository.ModuleRepository;
import org.motechproject.mots.validate.ValidationUtils;
import org.motechproject.mots.validate.annotations.ModulesExistence;
import org.springframework.beans.factory.annotation.Autowired;

public class ModulesExistenceValidator
    implements ConstraintValidator<ModulesExistence, Set<String>> {

  @Autowired
  private ModuleRepository moduleRepository;

  @Override
  public void initialize(ModulesExistence constraintAnnotation) {
  }

  @Override
  public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
    String notExistingModules = "";
    boolean uuidsValid = ValidationUtils.validateUuids(values);
    if (uuidsValid) {
      for (String moduleId : values) {
        if (!checkIfExist(moduleId)) {
          notExistingModules = notExistingModules.concat(moduleId + ", ");
        }
      }
      String message = String.format(
          ValidationMessages.NOT_EXISTING_MODULES_WITH_ID, notExistingModules);
      context.disableDefaultConstraintViolation();
      ValidationUtils.addDefaultViolationMessage(context, message);
      return notExistingModules.isEmpty();
    }
    return true;
  }

  private Boolean checkIfExist(String moduleId) {
    UUID module = UUID.fromString(moduleId);
    return moduleRepository.exists(module);
  }
}
