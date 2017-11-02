package org.motechproject.mots.domain;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.motechproject.mots.domain.enums.CallFlowElementType;

@Entity
@Table(name = "message")
@PrimaryKeyJoinColumn(name = "call_flow_element_id")
public class Message extends CallFlowElement {

  public Message() {
    super(CallFlowElementType.MESSAGE);
  }
}
