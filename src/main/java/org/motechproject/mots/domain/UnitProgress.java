package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

  @ManyToOne(fetch = FetchType.EAGER)
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
  @JoinColumn(name = "unit_progress_id", nullable = false)
  @Getter
  @Setter
  private Set<CallFlowElementLog> callFlowElementLogs;

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

  public void endUnit() {
    status = ProgressStatus.COMPLETED;
  }

  public boolean isCompleted() {
    return ProgressStatus.COMPLETED.equals(status);
  }

  /**
   * Change current call flow element, if no more elements change status to completed.
   */
  public void nextElement() {
    currentCallFlowElementNumber++;
    if (currentCallFlowElementNumber >= unit.getCallFlowElements().size()) {
      status = ProgressStatus.COMPLETED;
    }
  }

  /**
   * Change current call flow element to previous one.
   */
  public void previousElement() {
    if (currentCallFlowElementNumber > 0) {
      currentCallFlowElementNumber--;
      callFlowElementLogs.removeIf(callLog ->
          callLog.getCallFlowElement().getListOrder().equals(currentCallFlowElementNumber));
      status = ProgressStatus.IN_PROGRESS;
    }
  }

  /**
   * Get call flow elements starting from current element.
   * @return sublist of call flow elements
   */
  public List<CallFlowElement> getNotProcessedCallFlowElements() {
    List<CallFlowElement> callFlowElements = unit.getCallFlowElements();

    if (currentCallFlowElementNumber == 0) {
      return callFlowElements;
    }

    return callFlowElements.subList(currentCallFlowElementNumber, callFlowElements.size());
  }

  /**
   * Add Multiple Choice Question Log.
   * @param startDate question start date
   * @param endDate question end date
   * @param callFlowElement question that was responded to
   * @param choiceId chosen response number, null if no answer was chosen
   * @param numberOfAttempts number of times this question was listened
   */
  public void addMultipleChoiceQuestionLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement, Integer choiceId, Integer numberOfAttempts) {
    MultipleChoiceQuestionLog multipleChoiceQuestionLog = new MultipleChoiceQuestionLog(startDate,
        endDate, (MultipleChoiceQuestion) callFlowElement, choiceId, numberOfAttempts);

    if (callFlowElementLogs == null) {
      callFlowElementLogs = new HashSet<>();
    }

    callFlowElementLogs.add(multipleChoiceQuestionLog);
  }

  /**
   * Add Message Log.
   * @param startDate question start date
   * @param endDate question end date
   * @param callFlowElement message that was listened
   */
  public void addMessageLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement) {
    MessageLog messageLog = new MessageLog(startDate, endDate, callFlowElement);

    if (callFlowElementLogs == null) {
      callFlowElementLogs = new HashSet<>();
    }

    callFlowElementLogs.add(messageLog);
  }

  /**
   * Reset unit progress and increment number of replays.
   */
  public void resetProgressForUnitRepeat() {
    currentCallFlowElementNumber = 0;
    callFlowElementLogs.clear();
    status = ProgressStatus.NOT_STARTED;
    numberOfReplays++;
  }
}
