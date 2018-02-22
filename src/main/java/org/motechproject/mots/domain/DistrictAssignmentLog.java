package org.motechproject.mots.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.motechproject.mots.domain.security.User;

@Entity
@Table(name = "district_assignment_log")
@NoArgsConstructor
public class DistrictAssignmentLog extends BaseOwnershipEntity {

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @Getter
  @Setter
  @NonNull
  private District district;

  @Column(name = "start_date", nullable = false)
  @Getter
  @Setter
  @NonNull
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  @Getter
  @Setter
  @NonNull
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "module_id", nullable = false)
  @Getter
  @Setter
  @NonNull
  private Module module;

  /**
   * Constructor to build DistrictAssignmentLog.
   * @param district a district
   * @param startDate a startDate
   * @param endDate an endDate
   * @param module a module
   * @param owner an owner
   */
  public DistrictAssignmentLog(District district, LocalDate startDate, LocalDate endDate,
      Module module, User owner) {
    this.district = district;
    this.startDate = startDate;
    this.endDate = endDate;
    this.module = module;
    this.setOwner(owner);
  }
}
