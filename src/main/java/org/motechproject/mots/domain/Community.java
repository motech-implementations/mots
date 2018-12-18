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

@Entity
@Table(name = "community", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "facility_id"}))
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"facility"})
public class Community extends Location {

  @ManyToOne
  @JoinColumn(name = "facility_id", nullable = false)
  @Getter
  @Setter
  private Facility facility;

  public Community(UUID id) {
    super(id);
  }

  public Community(String name) {
    super(name);
  }

  public Community(String name, Facility facility) {
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
  public String getChiefdomName() {
    return facility.getChiefdom().getName();
  }
}
