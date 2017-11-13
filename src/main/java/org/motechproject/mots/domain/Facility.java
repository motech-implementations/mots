package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "facility")
public class Facility extends BaseEntity {

  @Column(name = "name", nullable = false)
  @Getter
  @Setter
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
}
