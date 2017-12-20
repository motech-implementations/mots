package org.motechproject.mots.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "community")
@NoArgsConstructor
@RequiredArgsConstructor
public class Community extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @ManyToOne
  @JoinColumn(name = "facility_id")
  @Getter
  @Setter
  private Facility facility;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Community community = (Community) o;

    return name.equals(community.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  public Community(UUID id) {
    super(id);
  }
}
