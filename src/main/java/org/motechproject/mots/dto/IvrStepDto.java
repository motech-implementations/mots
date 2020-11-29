package org.motechproject.mots.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class IvrStepDto {

  @Getter
  @Setter
  private String stepId;

  @Getter
  @Setter
  private String stepType;

  @Getter
  @Setter
  private String stepName;

  @Getter
  @Setter
  private String stepResult;

  @Getter
  @Setter
  private String stepData;

  @Getter
  @Setter
  private String error;

  @Getter
  @Setter
  private String entryAt;

  @Getter
  @Setter
  private String exitAt;
}
