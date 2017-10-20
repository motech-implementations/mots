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
public class Facility extends BaseEntity {

  @Column(nullable = false)
  @Getter
  @Setter
  private String name;

  @Column
  @Getter
  @Setter
  private String type;

  @OneToMany
  @JoinColumn(name = "facility_id")
  @Column
  @Getter
  @Setter
  private Set<Community> communities;
}
