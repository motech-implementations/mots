package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.motechproject.mots.domain.enums.ProgressStatus;

@Entity
@Table
public class UnitProgress extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "unit_id")
  @Getter
  @Setter
  private Unit unit;

  @OneToOne
  @JoinColumn(name = "current_call_flow_id")
  @Getter
  @Setter
  private CallFlowElement currentCallFlowElement;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private ProgressStatus status;

  @OneToMany
  @JoinColumn(name = "unit_id")
  @Column
  @Getter
  @Setter
  private Set<QuestionResponse> questionResponses;

  @Column(nullable = false)
  @Getter
  @Setter
  private Integer numberOfReplays;
}
