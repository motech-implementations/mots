package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

public class UnitDto extends IvrObjectDto {

  @Getter
  @Setter
  private String id;

  @NotBlank(message = "Unit Name cannot be empty")
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

  @NotNull(message = "Unit Allow Reply cannot be null")
  @Getter
  @Setter
  private Boolean allowReplay;
}
