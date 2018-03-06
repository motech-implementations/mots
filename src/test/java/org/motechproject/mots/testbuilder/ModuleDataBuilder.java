package org.motechproject.mots.testbuilder;

import java.util.UUID;
import org.motechproject.mots.domain.Module;

public class ModuleDataBuilder {
  private static int instanceNumber = 0;

  private UUID id;
  private String name;
  private String ivrGroup;

  /**
   * Returns instance of {@link ModuleDataBuilder} with sample data.
   */
  public ModuleDataBuilder() {
    instanceNumber++;

    id = UUID.randomUUID();
    name = "Module #" + instanceNumber;
  }

  /**
   * Builds instance of {@link Module} without id.
   */
  public Module buildAsNew() {

    Module module = new Module();
    module.setName(name);
    module.setIvrGroup(ivrGroup);

    return module;
  }

  /**
   * Builds instance of {@link Module}.
   */
  public Module build() {
    Module module = buildAsNew();
    module.setId(id);

    return module;
  }

  /**
   * Adds ivr group for new {@link Module}.
   */
  public ModuleDataBuilder withIvrGroup(String ivrGroup) {
    this.ivrGroup = ivrGroup;
    return this;
  }
}
