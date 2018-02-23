package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_response")
@NoArgsConstructor
public class QuestionResponse extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "question_id", nullable = false)
  @Getter
  @Setter
  private MultipleChoiceQuestion question;

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
   * Create new Question Response.
   * @param question question that was responded to
   * @param choiceId chosen response number
   * @param numberOfAttempts number of times this question was listened
   */
  public QuestionResponse(MultipleChoiceQuestion question, Integer choiceId,
      Integer numberOfAttempts) {
    this.question = question;
    this.numberOfAttempts = numberOfAttempts;

    if (choiceId != null) {
      this.chosenResponse = question.getChoices().get(choiceId);
    }
  }
}
