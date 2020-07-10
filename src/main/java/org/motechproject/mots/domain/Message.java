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

  private Message(String ivrId, String ivrName, Unit unit, String name, String content,
      CallFlowElementType type, Integer listOrder) {
    super(ivrId, ivrName, unit, name, content, type, listOrder);
  }

  @Override
  public Message copyAsNewDraft(Unit unit) {
    return new Message(getIvrId(), getIvrName(), unit, getName(),
        getContent(), getType(), getListOrder());
  }
}
