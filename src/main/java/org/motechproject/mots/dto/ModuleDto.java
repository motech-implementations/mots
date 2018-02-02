package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.Status;

public class ModuleDto extends IvrObjectDto {

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
  private String ivrGroup;

  @Getter
  @Setter
  private Integer moduleNumber;

  @Getter
  @Setter
  private Status status;

  @Getter
  @Setter
  private MultipleChoiceQuestionDto startModuleQuestion;

  @Getter
  @Setter
  private String type;

  @Getter
  @Setter
  private List<UnitDto> children;
}
