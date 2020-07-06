package org.motechproject.mots.validate.constraintvalidators;

import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.motechproject.mots.constants.ValidationMessageConstants;
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
    boolean uuidsValid = ValidationUtils.validateUuids(values);
    if (uuidsValid) {
      StringBuilder notExistingModules = new StringBuilder("");
      String delimiter = "";

      for (String moduleId : values) {
        if (!checkIfExist(moduleId)) {
          notExistingModules.append(delimiter).append(moduleId);
          delimiter = ", ";
        }
      }
      String message = String.format(
          ValidationMessageConstants.NOT_EXISTING_MODULES_WITH_ID, notExistingModules.toString());
      context.disableDefaultConstraintViolation();
      ValidationUtils.addDefaultViolationMessage(context, message);
      return notExistingModules.toString().isEmpty();
    }
    return true;
  }

  private Boolean checkIfExist(String moduleId) {
    UUID module = UUID.fromString(moduleId);
    return moduleRepository.existsById(module);
  }
}
