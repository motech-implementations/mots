package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "chiefdom", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "district_id"}))
@EqualsAndHashCode(callSuper = false, of = {"name", "district"})
public class Chiefdom extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @OneToMany(mappedBy = "chiefdom")
  @Getter
  @Setter
  @OrderBy(clause = "name ASC")
  private Set<Facility> facilities;

  @ManyToOne
  @JoinColumn(name = "district_id", nullable = false)
  @Getter
  @Setter
  private District district;
}
