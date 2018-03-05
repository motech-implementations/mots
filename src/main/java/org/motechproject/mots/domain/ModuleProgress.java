package org.motechproject.mots.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
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

  @Column(name = "current_unit_number", nullable = false)
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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinColumn(name = "module_progress_id", nullable = false)
  @SortComparator(UnitProgressComparator.class)
  @Getter
  @Setter
  private SortedSet<UnitProgress> unitsProgresses;

  @Column(name = "start_date")
  @Getter
  @Setter
  private LocalDateTime startDate;

  @Column(name = "end_date")
  @Getter
  @Setter
  private LocalDateTime endDate;

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
    this.currentUnitNumber = 0;
    this.unitsProgresses = module.getUnits().stream().map(UnitProgress::new).collect(
        Collectors.toCollection(() -> new TreeSet<>(new UnitProgressComparator())));
  }

  public UnitProgress getCurrentUnitProgress() {
    return new ArrayList<>(unitsProgresses).get(currentUnitNumber);
  }

  public boolean isCompleted() {
    return ProgressStatus.COMPLETED.equals(status);
  }

  /**
   * Change module status to in progress.
   */
  public void startModule(LocalDateTime startDate) {
    if (ProgressStatus.NOT_STARTED.equals(status)) {
      status = ProgressStatus.IN_PROGRESS;
      this.startDate = startDate;
    }
  }

  /**
   * Change current unit, if no more units change status to completed.
   */
  public void nextUnit(LocalDateTime endDate) {
    if (currentUnitNumber < module.getUnits().size() - 1) {
      currentUnitNumber++;
    } else {
      status = ProgressStatus.COMPLETED;
      this.endDate = endDate;
    }
  }

  public boolean isStarted() {
    return !ProgressStatus.NOT_STARTED.equals(status);
  }
}
