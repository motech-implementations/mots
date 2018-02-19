package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.Uuid;

public class UnitDto extends IvrObjectDto {

  @Uuid(message = ValidationMessages.INVALID_ID)
  @Getter
  @Setter
  private String id;

  @NotBlank(message = ValidationMessages.EMPTY_UNIT_NAME)
  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private String type;

  @Valid
  @Getter
  @Setter
  private List<CallFlowElementDto> children;

  @Valid
  @Getter
  @Setter
  private MultipleChoiceQuestionDto unitContinuationQuestion;

  @NotNull(message = ValidationMessages.NULL_UNIT_ALLOW_REPLAY)
  @Getter
  @Setter
  private Boolean allowReplay;
}
