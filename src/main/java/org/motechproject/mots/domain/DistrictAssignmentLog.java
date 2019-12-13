package org.motechproject.mots.domain;

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
  @JoinColumn(name = "district_id")
  @Getter
  @Setter
  @NonNull
  private District district;

  @ManyToOne
  @JoinColumn(name = "sector_id")
  @Getter
  @Setter
  private Sector sector;

  @ManyToOne
  @JoinColumn(name = "facility_id")
  @Getter
  @Setter
  private Facility facility;

  @ManyToOne
  @JoinColumn(name = "group_id")
  @Getter
  @Setter
  private Group group;

  @ManyToOne
  @JoinColumn(name = "module_id", nullable = false)
  @Getter
  @Setter
  @NonNull
  private Module module;

  /**
   * Constructor to build DistrictAssignmentLog.
   * @param district  a district
   * @param sector  a sector
   * @param facility  a facility
   * @param group     a group
   * @param module    a module
   * @param owner     an owner
   */
  public DistrictAssignmentLog(District district, Sector sector, Facility facility,
      Group group, Module module, User owner) {
    this.district = district;
    this.sector = sector;
    this.facility = facility;
    this.group = group;
    this.module = module;
    this.setOwner(owner);
  }
}
