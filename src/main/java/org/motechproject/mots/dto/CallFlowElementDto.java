package org.motechproject.mots.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.validate.annotations.CallFlowElementType;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MessageDto.class, name = "MESSAGE"),
    @JsonSubTypes.Type(value = MultipleChoiceQuestionDto.class, name = "QUESTION")
})
public abstract class CallFlowElementDto extends IvrObjectDto {

  @Getter
  @Setter
  private String id;

  @NotBlank(message = ValidationMessages.EMPTY_QUESTION_OR_MESSAGE)
  @Getter
  @Setter
  private String name;

  @Getter
  @Setter
  private String content;

  @CallFlowElementType
  @NotNull(message = ValidationMessages.EMPTY_CALL_FLOW_ELEMENT_TYPE)
  @Getter
  @Setter
  private String type;
}
