package org.motechproject.mots.dto;

import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@RequiredArgsConstructor
public class CallDetailRecordDto {

  @NotEmpty
  @NonNull
  @Getter
  @Setter
  private Map<String, String> ivrData;

}
