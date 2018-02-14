package org.motechproject.mots.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class JasperTemplateDto {

  @Getter
  @Setter
  private String id;

  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String type;

  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  private byte[] data;

  @Getter
  @Setter
  private List<String> requiredRights;

  @Getter
  @Setter
  private List<String> supportedFormats;

  @Getter
  @Setter
  private List<JasperTemplateParameterDto> templateParameters;
}
