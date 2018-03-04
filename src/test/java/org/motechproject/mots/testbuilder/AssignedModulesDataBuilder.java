package org.motechproject.mots.testbuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.Module;

public class AssignedModulesDataBuilder {

  private UUID id;
  private CommunityHealthWorker chw;
  private Set<Module> modules = new HashSet<>();

  /**
   * Returns instance of {@link AssignedModulesDataBuilder} with sample data.
   */
  public AssignedModulesDataBuilder() {
    id = UUID.randomUUID();
    chw = new CommunityHealthWorkerDataBuilder().build();
  }

  /**
   * Builds instance of {@link AssignedModules} without id.
   */
  public AssignedModules buildAsNew() {

    AssignedModules assignedModules = new AssignedModules();
    assignedModules.setHealthWorker(chw);
    assignedModules.setModules(modules);

    return assignedModules;
  }

  /**
   * Builds instance of {@link AssignedModules}.
   */
  public AssignedModules build() {
    AssignedModules assignedModules = buildAsNew();
    assignedModules.setId(id);

    return assignedModules;
  }

  /**
   * Adds Module for new {@link AssignedModules}.
   */
  public AssignedModulesDataBuilder withModule(Module module) {
    this.modules.add(module);
    return this;
  }

  /**
   * Adds CommunityHealthWorker for new {@link AssignedModules}.
   */
  public AssignedModulesDataBuilder withChw(CommunityHealthWorker chw) {
    this.chw = chw;
    return this;
  }
}
