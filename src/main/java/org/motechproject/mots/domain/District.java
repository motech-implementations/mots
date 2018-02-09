package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "district")
@EqualsAndHashCode(callSuper = false, of = "name")
public class District extends BaseEntity {

  @Column(name = "name", unique = true, nullable = false)
  @Getter
  @Setter
  @NonNull
  private String name;

  @OneToMany(mappedBy = "district")
  @Getter
  @Setter
  @OrderBy(clause = "name ASC")
  private Set<Chiefdom> chiefdoms;
}
