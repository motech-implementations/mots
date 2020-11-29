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

/**
 * This class represents state in which {@link Module} can be. This is used to determine
 *        how much user have listened. Also it hold information about current {@link Unit}.
 */
@Entity
@Table(name = "module_progress")
@NoArgsConstructor
public class ModuleProgress extends BaseTimestampedEntity {

  @ManyToOne
  @JoinColumn(name = "course_module_id", nullable = false)
  @Getter
  @Setter
  private CourseModule courseModule;

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

  @OneToMany(mappedBy = "moduleProgress", cascade = CascadeType.ALL, orphanRemoval = true)
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
   * @param courseModule Module which progress will be stored
   */
  public ModuleProgress(CommunityHealthWorker chw, CourseModule courseModule) {
    this.communityHealthWorker = chw;
    this.courseModule = courseModule;
    this.status = ProgressStatus.NOT_STARTED;
    this.interrupted = false;
    this.currentUnitNumber = 0;
    this.unitsProgresses = courseModule.getModule().getUnits().stream()
        .map(unit -> new UnitProgress(unit, this))
        .collect(Collectors.toCollection(() -> new TreeSet<>(new UnitProgressComparator())));
  }

  public UnitProgress getCurrentUnitProgress() {
    return new ArrayList<>(unitsProgresses).get(currentUnitNumber);
  }

  public boolean isCompleted() {
    return ProgressStatus.COMPLETED.equals(status);
  }

  public void startModule(LocalDateTime date) {
    status = ProgressStatus.IN_PROGRESS;
    this.startDate = date;
  }

  /**
   * Calculate module status. If module has not started the method sets it's status to in progress.
   *        If status is not completed and all unit progresses are completed then module status is
   *        also set to completed.
   *
   * @param date date of starting if the process has not started, in case of status
   *        is not completed ad all units status is completed date is ending date
   * @param currentUnitNumber number of currently processed unit
   */
  public void calculateModuleStatus(LocalDateTime date, Integer currentUnitNumber) {
    if (this.currentUnitNumber < currentUnitNumber) {
      this.currentUnitNumber = currentUnitNumber;
    }

    if (!ProgressStatus.COMPLETED.equals(status) && unitsProgresses.stream()
        .allMatch(unitProgress -> ProgressStatus.COMPLETED.equals(unitProgress.getStatus()))) {
      status = ProgressStatus.COMPLETED;
      this.endDate = date;
    }
  }

  /**
   * Change current unit, if no more units change status to completed.
   * @param endDate date of finish processing the unit
   * @param currentUnitNumber number of currently processed unit
   */
  public void nextUnit(LocalDateTime endDate, Integer currentUnitNumber) {
    this.currentUnitNumber = currentUnitNumber;

    if (currentUnitNumber < courseModule.getModule().getUnits().size() - 1) {
      this.currentUnitNumber++;
    } else {
      status = ProgressStatus.COMPLETED;
      this.endDate = endDate;
    }
  }

  public boolean isStarted() {
    return !ProgressStatus.NOT_STARTED.equals(status);
  }
}
