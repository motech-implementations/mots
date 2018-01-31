package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class MultipleChoiceQuestionDto extends CallFlowElementDto {

  @Getter
  @Setter
  private List<ChoiceDto> choices;
}
