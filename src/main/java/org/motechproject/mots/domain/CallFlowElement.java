package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessages;
import org.motechproject.mots.domain.enums.CallFlowElementType;

@Entity
@Table(name = "call_flow_element")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CallFlowElement extends IvrObject {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessages.EMPTY_QUESTION_OR_MESSAGE)
  private String name;

  @Type(type = "text")
  @Column(name = "content")
  @Getter
  @Setter
  private String content;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  private CallFlowElementType type;

  @Column(name = "list_order", nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

  public CallFlowElement(CallFlowElementType type) {
    this.type = type;
  }
}
