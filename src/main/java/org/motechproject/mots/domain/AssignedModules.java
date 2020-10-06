package org.motechproject.mots.domain;

import java.util.Collections;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents relation between {@link Module}s and {@link CommunityHealthWorker}. Single
 *        CommunityHealthWorker can be assigned to multiple modules.
 */
@Entity
@Table(name = "assigned_modules")
public class AssignedModules extends BaseTimestampedEntity {

  @OneToOne
  @JoinColumn(name = "health_worker_id")
  @Getter
  @Setter
  private CommunityHealthWorker healthWorker;

  @ManyToMany
  @JoinTable(name = "module_assignment",
      joinColumns = @JoinColumn(name = "assigned_modules_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "module_id", referencedColumnName = "id"))
  @JoinColumn(name = "assigned_modules_id")
  @Getter
  @Setter
  private Set<Module> modules;

  public AssignedModules() {
    super();
  }

  public AssignedModules(CommunityHealthWorker healthWorker) {
    this.healthWorker = healthWorker;
    this.modules = Collections.emptySet();
  }

  public void unassignModule(Module module) {
    modules.removeIf(m -> m.getId().equals(module.getId()));
  }
}
