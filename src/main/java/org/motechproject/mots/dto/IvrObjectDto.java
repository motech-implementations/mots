package org.motechproject.mots.dto;

import lombok.Getter;
import lombok.Setter;

public abstract class IvrObjectDto {

  @Getter
  @Setter
  private String ivrId;

  @Getter
  @Setter
  private String ivrName;
}
