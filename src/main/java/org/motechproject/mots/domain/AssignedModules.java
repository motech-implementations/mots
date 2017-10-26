package org.motechproject.mots.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
public class AssignedModules extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "health_worker_id")
  @Getter
  @Setter
  private CommunityHealthWorker healthWorker;

  @OneToMany
  @JoinColumn(name = "assigned_modules_id")
  @Column
  @Getter
  @Setter
  private Set<ModuleProgress> modules;
}
