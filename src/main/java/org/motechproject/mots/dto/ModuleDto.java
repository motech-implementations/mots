package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class ModuleDto extends IvrObjectDto {

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private String ivrGroup;

  @Getter
  @Setter
  private Integer moduleNumber;

  @Getter
  @Setter
  private MultipleChoiceQuestionDto startModuleQuestion;

  @Getter
  @Setter
  private List<UnitDto> units;
}
