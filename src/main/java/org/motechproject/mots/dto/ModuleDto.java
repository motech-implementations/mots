package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.validate.annotations.Uuid;

public class ModuleDto extends IvrObjectDto {

  @Uuid(message = ValidationMessages.INVALID_ID)
  @Getter
  @Setter
  private String id;

  @NotBlank(message = ValidationMessages.EMPTY_MODULE_NAME)
  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private String ivrGroup;

  @Min(value = 1, message = ValidationMessages.MODULE_NUMBER_LESS_THAN_ONE)
  @NotNull(message = ValidationMessages.EMPTY_MODULE_NUMBER)
  @Getter
  @Setter
  private Integer moduleNumber;

  @Getter
  @Setter
  private Status status;

  @Valid
  @Getter
  @Setter
  private MultipleChoiceQuestionDto startModuleQuestion;

  @Getter
  @Setter
  private String type;

  @Valid
  @Getter
  @Setter
  private List<UnitDto> children;
}
