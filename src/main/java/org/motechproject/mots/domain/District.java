package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "district")
public class District extends BaseEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @OneToMany(mappedBy = "district")
  @Getter
  @Setter
  private Set<Chiefdom> chiefdoms;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    District district = (District) o;

    return name != null ? name.equals(district.name) : district.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
