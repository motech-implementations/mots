package org.motechproject.mots.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SortComparator;
import org.motechproject.mots.domain.enums.ProgressStatus;
import org.motechproject.mots.utils.UnitProgressComparator;

@Entity
@Table(name = "module_progress")
@NoArgsConstructor
public class ModuleProgress extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "module_id", nullable = false)
  @Getter
  @Setter
  private Module module;

  @ManyToOne
  @JoinColumn(name = "chw_id", nullable = false)
  @Getter
  @Setter
  private CommunityHealthWorker communityHealthWorker;

  @Column(name = "current_unit_number")
  @Getter
  @Setter
  private Integer currentUnitNumber;

  @Column(name = "interrupted", nullable = false)
  @Getter
  @Setter
  private Boolean interrupted;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @Getter
  @Setter
  private ProgressStatus status;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "module_id")
  @SortComparator(UnitProgressComparator.class)
  @Getter
  @Setter
  @Valid
  private List<UnitProgress> unitsProgresses;

  /**
   * Create new Module Progress.
   * @param chw CHW which progress will be stored
   * @param module Module which progress will be stored
   */
  public ModuleProgress(CommunityHealthWorker chw, Module module) {
    this.communityHealthWorker = chw;
    this.module = module;
    this.status = ProgressStatus.NOT_STARTED;
    this.interrupted = false;
    this.unitsProgresses = module.getUnits().stream().map(UnitProgress::new).collect(
        Collectors.toList());
  }
}
