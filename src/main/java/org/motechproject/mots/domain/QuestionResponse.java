package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "question_response")
public class QuestionResponse extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "question_id", nullable = false)
  @Getter
  @Setter
  private MultipleChoiceQuestion question;

  @ManyToOne
  @JoinColumn(name = "response_id", nullable = false)
  @Getter
  @Setter
  private Choice chosenResponse;

  @Column(name = "number_of_attempts", nullable = false)
  @Getter
  @Setter
  private Integer numberOfAttempts;
}
