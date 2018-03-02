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

@NoArgsConstructor
@Entity
@Table(name = "chiefdom", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "district_id"}))
@EqualsAndHashCode(callSuper = true, of = {"district"})
public class Chiefdom extends Location {

  @OneToMany(mappedBy = "chiefdom")
  @Getter
  @Setter
  @OrderBy("name ASC")
  private Set<Facility> facilities;

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @Getter
  @Setter
  private District district;

  public Chiefdom(UUID id) {
    super(id);
  }

  public Chiefdom(String name) {
    super(name);
  }

  @Override
  public String getParentName() {
    return district.getName();
  }
}
