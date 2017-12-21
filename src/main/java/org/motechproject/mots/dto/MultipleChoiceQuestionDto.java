package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class MultipleChoiceQuestionDto extends CallFlowElementDto {

  @Getter
  @Setter
  private Set<ChoiceDto> choices;
}
