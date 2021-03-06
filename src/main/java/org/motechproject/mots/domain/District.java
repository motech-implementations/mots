package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a higher order location entity. District can have multiple sectors.
 */
@NoArgsConstructor
@Entity
@Table(name = "district", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class District extends Location {

  @Column(name = "ivr_group_id")
  @Getter
  @Setter
  private String ivrGroupId;

  @OneToMany(mappedBy = "district")
  @Getter
  @Setter
  @OrderBy("name ASC")
  private Set<Sector> sectors;

  public District(UUID id) {
    super(id);
  }

  public District(String name) {
    super(name);
  }

  @Override
  public String getParentName() {
    return null;
  }
}
