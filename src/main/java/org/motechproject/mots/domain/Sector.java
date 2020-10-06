package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
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

/**
 * This class represents an area to which multiple facilities {@link Facility} can belong to.
 */
@NoArgsConstructor
@Entity
@Table(name = "sector", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "district_id"}))
@EqualsAndHashCode(callSuper = true, of = {"district"})
public class Sector extends Location {

  @OneToMany(mappedBy = "sector")
  @Getter
  @Setter
  @OrderBy("name ASC")
  private Set<Facility> facilities;

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @Getter
  @Setter
  private District district;

  public Sector(UUID id) {
    super(id);
  }

  public Sector(String name) {
    super(name);
  }

  public Sector(String name, District district) {
    super(name);
    this.district = district;
  }

  @Override
  public String getParentName() {
    return district.getName();
  }
}
