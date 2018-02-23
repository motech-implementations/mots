package org.motechproject.mots.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.enums.ProgressStatus;

@Entity
@Table(name = "unit_progress")
@NoArgsConstructor
public class UnitProgress extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
  @Getter
  @Setter
  private Unit unit;

  @Column(name = "current_call_flow_element_number", nullable = false)
  @Getter
  @Setter
  private Integer currentCallFlowElementNumber;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private ProgressStatus status;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "unit_id")
  @Getter
  @Setter
  private Set<QuestionResponse> questionResponses;

  @Column(name = "number_of_replays", nullable = false)
  @Getter
  @Setter
  private Integer numberOfReplays;

  /**
   * Create new Unit Progress.
   * @param unit Unit which progress will be stored
   */
  public UnitProgress(Unit unit) {
    this.unit = unit;
    this.status = ProgressStatus.NOT_STARTED;
    this.numberOfReplays = 0;
    this.currentCallFlowElementNumber = 0;
  }

  public void startUnit() {
    status = ProgressStatus.IN_PROGRESS;
  }

  /**
   * Change current call flow element, if no more elements change status to completed.
   */
  public void nextElement() {
    if (currentCallFlowElementNumber < unit.getCallFlowElements().size() - 1) {
      currentCallFlowElementNumber++;
    } else {
      status = ProgressStatus.COMPLETED;
    }
  }

  /**
   * Add response to a question.
   * @param callFlowElement question that was responded to
   * @param choiceId chosen response number, null if no answer was chosen
   * @param numberOfAttempts number of times this question was listened
   */
  public void addQuestionResponse(CallFlowElement callFlowElement, Integer choiceId,
      Integer numberOfAttempts) {
    QuestionResponse questionResponse =
        new QuestionResponse((MultipleChoiceQuestion) callFlowElement, choiceId, numberOfAttempts);

    if (questionResponses == null) {
      questionResponses = new HashSet<>();
    }

    questionResponses.add(questionResponse);
  }
}
