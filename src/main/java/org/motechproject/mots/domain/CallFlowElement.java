package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.domain.enums.CallFlowElementType;

/**
 * This class is a base class for all IVR interactive classes (e.g. {@link Message},
 *        {@link MultipleChoiceQuestion}) which are the actual content in the Viamo
 *        (the audio files played to the user).
 */
@Entity
@Table(name = "call_flow_element")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CallFlowElement extends IvrObject {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NotBlank(message = ValidationMessageConstants.EMPTY_QUESTION_OR_MESSAGE)
  private String name;

  @ManyToOne
  @JoinColumn(name = "unit_id")
  @Getter
  @Setter
  private Unit unit;

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

  /**
   * Create new Call Flow Element.
   * @param ivrId ivr id of element
   * @param ivrName ivr name of element
   * @param unit unit of element
   * @param name name of element
   * @param content content of element
   * @param type type of element
   * @param listOrder order of element
   */
  public CallFlowElement(String ivrId, String ivrName, Unit unit, String name, String content,
      CallFlowElementType type, Integer listOrder) {
    super(ivrId, ivrName);
    this.unit = unit;
    this.name = name;
    this.content = content;
    this.type = type;
    this.listOrder = listOrder;
  }

  public abstract CallFlowElement copyAsNewDraft(Unit unit);
}
