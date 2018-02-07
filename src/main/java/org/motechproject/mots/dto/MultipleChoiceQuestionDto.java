package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

public class MultipleChoiceQuestionDto extends CallFlowElementDto {

  @Valid
  @Getter
  @Setter
  private List<ChoiceDto> choices;
}
