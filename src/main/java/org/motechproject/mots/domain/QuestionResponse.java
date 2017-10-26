package org.motechproject.mots.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class QuestionResponse extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "question_id")
  @Getter
  @Setter
  private MultipleChoiceQuestion question;

  @OneToOne
  @JoinColumn(name = "response_id")
  @Getter
  @Setter
  private Choice chosenResponse;

  @Column(nullable = false)
  @Getter
  @Setter
  private Integer numberOfAttempts;
}
