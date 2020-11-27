package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.motechproject.mots.domain.enums.FacilityType;

/**
 * This class represents a medical center that multiple {@link Village}s can belong to.
 */
@Entity
@Table(name = "facility", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "sector_id"}))
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = { "sector" })
public class Facility extends Location {

  @Column(name = "type")
  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private FacilityType type;

  @OneToMany(mappedBy = "facility")
  @Getter
  @Setter
  @OrderBy("name ASC")
  private Set<Village> villages;

  @ManyToOne
  @JoinColumn(name = "sector_id", nullable = false)
  @Getter
  @Setter
  private Sector sector;

  @Column(name = "incharge_full_name")
  @Getter
  @Setter
  private String inchargeFullName;

  @Column(name = "incharge_phone")
  @Getter
  @Setter
  private String inchargePhone;

  @Column(name = "incharge_email")
  @Getter
  @Setter
  private String inchargeEmail;

  public Facility(UUID id) {
    super(id);
  }

  /**
   * Construct facility by first calling super constructor, and then setting type and facilityId.
   * @param name name of the facility
   * @param facilityType type of the facility
   */
  public Facility(String name, FacilityType facilityType) {
    super(name);
    this.type = facilityType;
  }

  /**
   * Construct new facility with given parameters.
   * @param name name of the facility
   * @param type type of the facility
   * @param inchargeFullName name of the facility incharge
   * @param inchargePhone phone of the facility incharge
   * @param inchargeEmail email of the facility incharge
   * @param sector parent sector of the facility
   */
  public Facility(String name, FacilityType type, String inchargeFullName,
      String inchargePhone, String inchargeEmail, Sector sector) {
    super(name);
    this.type = type;
    this.inchargeFullName = inchargeFullName;
    this.inchargePhone = inchargePhone;
    this.inchargeEmail = inchargeEmail;
    this.sector = sector;
  }

  @Override
  public String getParentName() {
    return sector.getName();
  }

  @Override
  public String getDistrictName() {
    return sector.getDistrict().getName();
  }

  @Override
  public FacilityType getFacilityType() {
    return getType();
  }
}
