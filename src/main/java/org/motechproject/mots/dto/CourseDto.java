package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.domain.enums.Status;

public class CourseDto extends IvrObjectDto {

  @Getter
  @Setter
  private String id;

  @NotBlank(message = "Course Name cannot be empty")
  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private String ivrGroup;

  @Min(value = 0, message = "Module Number cannot be negative")
  @NotNull(message = "Module Number cannot be empty")
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
