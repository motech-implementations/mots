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
import org.motechproject.mots.domain.enums.CallFlowElementType;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CallFlowElement extends IvrObject {

  @Column(nullable = false)
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column
  @Getter
  @Setter
  private String content;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  private CallFlowElementType type;

  @Column(nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

  public CallFlowElement(CallFlowElementType type) {
    this.type = type;
  }
}
