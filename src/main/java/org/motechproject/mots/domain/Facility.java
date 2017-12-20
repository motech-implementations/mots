package org.motechproject.mots.domain;

import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facility")
@NoArgsConstructor
@RequiredArgsConstructor
public class Facility extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @Column(name = "type")
  @Getter
  @Setter
  private String type;

  @OneToMany(mappedBy = "facility")
  @Getter
  @Setter
  private Set<Community> communities;

  @ManyToOne
  @JoinColumn(name = "chiefdom_id")
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Facility facility = (Facility) o;

    return name.equals(facility.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
