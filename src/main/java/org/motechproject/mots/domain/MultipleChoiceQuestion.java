package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.CallFlowElementType;

@Entity
@Table
@PrimaryKeyJoinColumn(name = "call_flow_element_id")
public class MultipleChoiceQuestion extends CallFlowElement {

  @OneToMany
  @JoinColumn(name = "question_id")
  @Column
  @Getter
  @Setter
  private Set<Choice> choices;

  public MultipleChoiceQuestion() {
    super(CallFlowElementType.QUESTION);
  }
}
