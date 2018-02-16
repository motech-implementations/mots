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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.motechproject.mots.domain.enums.FacilityType;

@Entity
@Table(name = "facility", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "chiefdom_id"}))
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = { "chiefdom" })
public class Facility extends Location {

  @Column(name = "type", nullable = false)
  @Getter
  @Setter
  @NonNull
  @Enumerated(EnumType.STRING)
  private FacilityType type;

  @Column(name = "facility_id", nullable = false, unique = true)
  @Getter
  @Setter
  @NonNull
  private String facilityId;

  @OneToMany(mappedBy = "facility")
  @Getter
  @Setter
  @OrderBy("name ASC")
  private Set<Community> communities;

  @ManyToOne
  @JoinColumn(name = "chiefdom_id", nullable = false)
  @Getter
  @Setter
  private Chiefdom chiefdom;

  @OneToOne(mappedBy = "facility")
  @Getter
  @Setter
  private Incharge incharge;

  public Facility(UUID id) {
    super(id);
  }

  /**
   * Construct facility by first calling super constructor, and then setting type and facilityId.
   * @param name name of the facility
   * @param facilityType type of the facility
   * @param facilityId id of the facility
   */
  public Facility(String name, FacilityType facilityType, String facilityId) {
    super(name);
    this.type = facilityType;
    this.facilityId = facilityId;
  }

  @Override
  public String getParentName() {
    return chiefdom.getName();
  }

  @Override
  public FacilityType getFacilityType() {
    return getType();
  }

  @Override
  public String getInchargeFullName() {
    if (incharge == null) {
      return null;
    }
    return incharge.getFullName();
  }
}
