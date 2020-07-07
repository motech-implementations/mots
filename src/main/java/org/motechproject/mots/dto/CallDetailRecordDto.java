package org.motechproject.mots.dto;

import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class CallDetailRecordDto {

  @NotEmpty
  @NonNull
  @Getter
  @Setter
  private Map<String, String> ivrData;

}
