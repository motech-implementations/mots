package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

/**
 *  This class is used to track progress of {@link Module}/{@link Course}/{@link Unit}.
 */
@Entity
@Table(name = "message_log")
@PrimaryKeyJoinColumn(name = "call_flow_element_log_id")
@NoArgsConstructor
public class MessageLog extends CallFlowElementLog {

  public MessageLog(LocalDateTime startDate, LocalDateTime endDate,
      CallFlowElement callFlowElement) {
    super(startDate, endDate, callFlowElement);
  }
}
