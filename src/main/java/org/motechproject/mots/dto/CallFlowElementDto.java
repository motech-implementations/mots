package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MessageDto.class, name = "MESSAGE"),
    @JsonSubTypes.Type(value = MultipleChoiceQuestionDto.class, name = "QUESTION")
})
public abstract class CallFlowElementDto extends IvrObjectDto {

  @Getter
  @Setter
  private String id;

  @NotBlank(message = "Message/Question name cannot be empty")
  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String content;

  @Getter
  @Setter
  private String type;
}
