package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents the lowest order location entity in location hierarchy.
 */
@Entity
@Table(name = "village", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "facility_id"}))
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"facility"})
public class Village extends Location {

  @ManyToOne
  @JoinColumn(name = "facility_id", nullable = false)
  @Getter
  @Setter
  private Facility facility;

  public Village(UUID id) {
    super(id);
  }

  public Village(String name) {
    super(name);
  }

  public Village(String name, Facility facility) {
    super(name);
    this.facility = facility;
  }

  @Override
  public String getParentName() {
    return facility.getName();
  }

  @Override
  public String getDistrictName() {
    return facility.getDistrictName();
  }

  @Override
  public String getSectorName() {
    return facility.getSector().getName();
  }
}
