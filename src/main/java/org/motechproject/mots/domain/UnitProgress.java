package org.motechproject.mots.domain;

import java.util.Set;
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
public class UnitProgress extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "unit_id", nullable = false)
  @Getter
  @Setter
  private Unit unit;

  @Column(name = "current_call_flow_element_number")
  @Getter
  @Setter
  private Integer currentCallFlowElementNumber;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private ProgressStatus status;

  @OneToMany
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
  }
}
