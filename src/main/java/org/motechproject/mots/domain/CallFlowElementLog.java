package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "call_flow_element_log")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class CallFlowElementLog extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "call_flow_element_id", nullable = false)
  @Getter
  @Setter
  private CallFlowElement callFlowElement;

  @Column(name = "start_date")
  @Getter
  @Setter
  private LocalDateTime startDate;

  @Column(name = "end_date")
  @Getter
  @Setter
  private LocalDateTime endDate;

  /**
   * Create new Call Flow Element Log.
   * @param startDate date when question started
   * @param endDate end date od the question
   * @param callFlowElement element for which log is created
   */
  public CallFlowElementLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.callFlowElement = callFlowElement;
  }
}
