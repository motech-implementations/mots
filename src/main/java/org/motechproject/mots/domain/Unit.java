package org.motechproject.mots.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "unit")
public class Unit extends IvrObject {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  private String name;

  @Type(type = "text")
  @Column(name = "description")
  @Getter
  @Setter
  private String description;

  @Column(name = "list_order", nullable = false)
  @Getter
  @Setter
  private Integer listOrder;

  @OneToMany
  @JoinColumn(name = "unit_id")
  @OrderBy("list_order ASC")
  @Getter
  @Setter
  private List<CallFlowElement> callFlowElements;

  @OneToOne
  @JoinColumn(name = "continuation_question_id")
  @Getter
  @Setter
  private MultipleChoiceQuestion unitContinuationQuestion;

  @Column(name = "allow_replay", nullable = false)
  @Getter
  @Setter
  private Boolean allowReplay;
}
