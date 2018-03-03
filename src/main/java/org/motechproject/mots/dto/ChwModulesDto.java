package org.motechproject.mots.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class ChwModulesDto {

  @Getter
  @Setter
  private CommunityHealthWorkerDto chw;

  @Getter
  @Setter
  private Set<ModuleSimpleDto> modules;
}
