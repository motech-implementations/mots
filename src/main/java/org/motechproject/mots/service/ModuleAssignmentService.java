package org.motechproject.mots.service;

import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleAssignmentService {

  @Autowired
  private AssignedModulesRepository repository;

  /**
   * Assign modules to CHW.
   * @param assignedModules modules assigned for CHW
   */
  public void assignModules(AssignedModules assignedModules) {
    AssignedModules existingAssignedModules =
        repository.findByHealthWorkerId(assignedModules.getHealthWorker().getId());

    if (existingAssignedModules != null) {
      existingAssignedModules.setModules(assignedModules.getModules());
    } else {
      existingAssignedModules = assignedModules;
    }

    repository.save(existingAssignedModules);
  }
}
