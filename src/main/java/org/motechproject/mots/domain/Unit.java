package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "unit_id")
  @OrderBy("list_order ASC")
  @Getter
  private List<CallFlowElement> callFlowElements;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "continuation_question_id")
  @Getter
  @Setter
  private MultipleChoiceQuestion unitContinuationQuestion;

  @Column(name = "allow_replay", nullable = false)
  @Getter
  @Setter
  private Boolean allowReplay;

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
}
