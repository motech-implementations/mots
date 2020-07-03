package org.motechproject.mots.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.CourseReleaseCheck;

@Entity
@Table(name = "unit")
public class Unit extends IvrObject {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "list_order", nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

  @ManyToOne
  @JoinColumn(name = "module_id")
  @Getter
  @Setter
  private Module module;

  @Valid
  @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("list_order ASC")
  @Getter
  private List<CallFlowElement> callFlowElements;

  @NotBlank(message = ValidationMessageConstants.EMPTY_CONTINUATION_QUESTION_IVR_ID,
      groups = CourseReleaseCheck.class)
  @Column(name = "continuation_question_ivr_id")
  @Getter
  @Setter
  private String continuationQuestionIvrId;

  @Column(name = "allow_replay", nullable = false)
  @Getter
  @Setter
  private Boolean allowReplay;

  public Unit() {
  }

  private Unit(String ivrId, String ivrName, String name, String description,
      Integer listOrder,
      List<CallFlowElement> callFlowElements,
      String continuationQuestionIvrId, Boolean allowReplay) {
    super(ivrId, ivrName);
    this.name = name;
    this.description = description;
    this.listOrder = listOrder;
    this.callFlowElements = callFlowElements;
    this.continuationQuestionIvrId = continuationQuestionIvrId;
    this.allowReplay = allowReplay;
  }

  /**
   * Update list content.
   * @param callFlowElements list of new Call Flow Elements
   */
  public void setCallFlowElements(List<CallFlowElement> callFlowElements) {
    if (this.callFlowElements == null) {
      this.callFlowElements = callFlowElements;
    } else if (!this.callFlowElements.equals(callFlowElements)) {
      this.callFlowElements.clear();

      if (callFlowElements != null) {
        this.callFlowElements.addAll(callFlowElements);
      }
    }
  }

  /**
   * Create a drat copy of Unit.
   * @return copy of Unit
   */
  public Unit copyAsNewDraft() {
    List<CallFlowElement> callFlowElementsCopy = new ArrayList<>();

    if (callFlowElements != null) {
      callFlowElements.forEach(callFlowElement ->
          callFlowElementsCopy.add(callFlowElement.copyAsNewDraft()));
    }

    return new Unit(getIvrId(), getIvrName(), name, description, listOrder, callFlowElementsCopy,
        continuationQuestionIvrId, allowReplay);
  }
}
