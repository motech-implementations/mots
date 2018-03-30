package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.Status;
import org.motechproject.mots.validate.annotations.Uuid;

public class CourseDto extends IvrObjectDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  private String treeId;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private Status status;

  @Getter
  @Setter
  private String type;

  @Valid
  @Getter
  @Setter
  private List<ModuleDto> children;
}
