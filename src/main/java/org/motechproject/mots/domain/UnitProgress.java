package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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

  @ManyToOne
  @JoinColumn(name = "module_progress_id", nullable = false)
  @Getter
  @Setter
  private ModuleProgress moduleProgress;

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
   * @param moduleProgress parent module progress
   */
  public UnitProgress(Unit unit, ModuleProgress moduleProgress) {
    this.unit = unit;
    this.moduleProgress = moduleProgress;
    this.status = ProgressStatus.NOT_STARTED;
    this.numberOfReplays = 0;
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
   * Get call flow elements list iterator starting from current element.
   * @return call flow elements list iterator
   */
  public ListIterator<CallFlowElement> getCallFlowElementsIterator() {
    List<CallFlowElement> callFlowElements = unit.getCallFlowElements();

    return callFlowElements.listIterator();
  }

  /**
   * Add Multiple Choice Question Log.
   * @param startDate question start date
   * @param endDate question end date
   * @param callFlowElement question that was responded to
   * @param choice chosen response, null if no answer was chosen
   * @param numberOfAttempts number of times this question was listened
   */
  public void addOrUpdateMultipleChoiceQuestionLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement, Choice choice, Integer numberOfAttempts) {

    if (callFlowElementLogs == null) {
      callFlowElementLogs = new HashSet<>();
    }

    MultipleChoiceQuestionLog multipleChoiceQuestionLog =
        (MultipleChoiceQuestionLog) findCallFlowElementLog(callFlowElement);

    if (multipleChoiceQuestionLog == null) {
      multipleChoiceQuestionLog = new MultipleChoiceQuestionLog(startDate,
          endDate, (MultipleChoiceQuestion) callFlowElement, choice, numberOfAttempts);
      callFlowElementLogs.add(multipleChoiceQuestionLog);
    } else if (choice != null || multipleChoiceQuestionLog.getChosenResponse() == null) {
      multipleChoiceQuestionLog.setStartDate(startDate);
      multipleChoiceQuestionLog.setEndDate(endDate);
      multipleChoiceQuestionLog.setChosenResponse(choice);
      multipleChoiceQuestionLog.setNumberOfAttempts(numberOfAttempts);
    }
  }

  /**
   * Add Message Log.
   * @param startDate question start date
   * @param endDate question end date
   * @param callFlowElement message that was listened
   */
  public void addOrUpdateMessageLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement) {

    if (callFlowElementLogs == null) {
      callFlowElementLogs = new HashSet<>();
    }

    MessageLog messageLog = (MessageLog) findCallFlowElementLog(callFlowElement);

    if (messageLog == null) {
      messageLog = new MessageLog(startDate, endDate, callFlowElement);
      callFlowElementLogs.add(messageLog);
    } else {
      messageLog.setStartDate(startDate);
      messageLog.setEndDate(endDate);
    }
  }

  /**
   * Reset unit progress and increment number of replays.
   */
  public void resetProgressForUnitRepeat() {
    status = ProgressStatus.NOT_STARTED;
    numberOfReplays++;
  }

  private CallFlowElementLog findCallFlowElementLog(CallFlowElement callFlowElement) {
    for (CallFlowElementLog element : callFlowElementLogs) {
      if (callFlowElement.getIvrId().equals(element.getCallFlowElement().getIvrId())) {
        return element;
      }
    }

    return null;
  }
}
