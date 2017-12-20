package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class UnitDto extends IvrObjectDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private List<CallFlowElementDto> callFlowElements;

  @Getter
  @Setter
  private MultipleChoiceQuestionDto unitContinuationQuestion;

  @Getter
  @Setter
  private Boolean allowReplay;
}
