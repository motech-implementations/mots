package org.motechproject.mots.dto;

import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.annotations.Uuid;

public class UnitDto extends IvrObjectDto {

  @Getter
  @Setter
  @Uuid
  private String id;

  @Getter
  @Setter
  private String treeId;

  @NotBlank(message = ValidationMessageConstants.EMPTY_UNIT_NAME)
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

  @Getter
  @Setter
  private String continuationQuestionIvrId;

  @Getter
  @Setter
  private Boolean allowReplay;
}
