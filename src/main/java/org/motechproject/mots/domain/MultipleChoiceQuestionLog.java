package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "multiple_choice_question_log")
@PrimaryKeyJoinColumn(name = "call_flow_element_log_id")
@NoArgsConstructor
public class MultipleChoiceQuestionLog extends CallFlowElementLog {

  @ManyToOne
  @JoinColumn(name = "response_id")
  @Getter
  @Setter
  private Choice chosenResponse;

  @Column(name = "number_of_attempts", nullable = false)
  @Getter
  @Setter
  private Integer numberOfAttempts;

  /**
   * Create new Multiple Choice Question Log.
   * @param startDate date when question started
   * @param endDate end date od the question
   * @param question question that was responded to
   * @param choiceId chosen response number (starts from 1)
   * @param numberOfAttempts number of times this question was listened
   */
  public MultipleChoiceQuestionLog(LocalDateTime startDate, LocalDateTime endDate,
      MultipleChoiceQuestion question, Integer choiceId, Integer numberOfAttempts) {
    super(startDate, endDate, question);

    this.numberOfAttempts = numberOfAttempts;

    if (choiceId != null) {
      this.chosenResponse = question.getChoices().get(choiceId - 1);
    }
  }
}
