package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class Chiefdom extends BaseEntity {

  @Column(unique = true, nullable = false)
  @Getter
  @Setter
  private String name;

  @OneToMany
  @JoinColumn(name = "chiefdom_id")
  @Column
  @Getter
  @Setter
  private Set<Facility> facilities;
}
