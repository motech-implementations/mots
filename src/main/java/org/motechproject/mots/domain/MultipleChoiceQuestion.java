package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.Getter;
import org.motechproject.mots.domain.enums.CallFlowElementType;

@Entity
@Table(name = "multiple_choice_question")
@PrimaryKeyJoinColumn(name = "call_flow_element_id")
public class MultipleChoiceQuestion extends CallFlowElement {

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "question_id")
  @OrderBy("ivr_pressed_key ASC")
  @Getter
  @Valid
  private List<Choice> choices;

  public MultipleChoiceQuestion() {
    super(CallFlowElementType.QUESTION);
  }

  /**
   * Update list content.
   * @param choices list of new Choices
   */
  public void setChoices(List<Choice> choices) {
    if (this.choices == null) {
      this.choices = choices;
    } else if (!this.choices.equals(choices)) {
      this.choices.clear();

      if (choices != null) {
        this.choices.addAll(choices);
      }
    }
  }
}
