package org.motechproject.mots.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class IvrCallLogDto {

  @Getter
  @Setter
  private String logId;

  @Getter
  @Setter
  private String chwIvrId;

  @Getter
  @Setter
  private List<IvrStepDto> interactions;
}
