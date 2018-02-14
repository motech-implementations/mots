package org.motechproject.mots.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JasperTemplateParameterDto {

  private String id;
  private String name;
  private String displayName;
  private String defaultValue;
  private String dataType;
  private String selectExpression;
  private String selectMethod;
  private String selectBody;
  private String selectProperty;
  private String displayProperty;
  private String description;
  private Boolean required;
  private List<String> options;
}
