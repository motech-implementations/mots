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
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.motechproject.mots.constants.ValidationMessageConstants;
import org.motechproject.mots.validate.CourseReleaseCheck;

/**
 * This class represents lowest level call flow area. Units contains {@link CallFlowElement}s
 *        that are used to communicate between {@link CommunityHealthWorker} and the IVR system.
 */
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

  private Unit(String ivrId, String ivrName, Module module, String name, String description,
      Integer listOrder, String continuationQuestionIvrId, Boolean allowReplay) {
    super(ivrId, ivrName);
    this.module = module;
    this.name = name;
    this.description = description;
    this.listOrder = listOrder;
    this.continuationQuestionIvrId = continuationQuestionIvrId;
    this.allowReplay = allowReplay;
  }

  /**
   * Update list content.
   *
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
   * Create a draft copy of Unit.
   *
   * @param module module to copy
   * @return copy of Unit
   */
  public Unit copyAsNewDraft(Module module) {
    Unit unitCopy = new Unit(getIvrId(), getIvrName(), module, name, description, listOrder,
        continuationQuestionIvrId, allowReplay);

    List<CallFlowElement> callFlowElementsCopy = new ArrayList<>();

    if (callFlowElements != null) {
      callFlowElements.forEach(el -> callFlowElementsCopy.add(el.copyAsNewDraft(unitCopy)));
    }

    unitCopy.setCallFlowElements(callFlowElementsCopy);
    return unitCopy;
  }
}
