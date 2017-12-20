package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "chiefdom")
public class Chiefdom extends BaseEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @OneToMany(mappedBy = "chiefdom")
  @Getter
  @Setter
  private Set<Facility> facilities;

  @ManyToOne
  @JoinColumn(name = "district_id")
  @Getter
  @Setter
  private District district;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Chiefdom chiefdom = (Chiefdom) o;

    return name.equals(chiefdom.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
