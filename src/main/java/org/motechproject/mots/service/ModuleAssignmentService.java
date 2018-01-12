package org.motechproject.mots.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.Module;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.exception.ModuleAssignmentException;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModuleAssignmentService {

  @Autowired
  private AssignedModulesRepository repository;

  @Autowired
  private IvrService ivrService;

  @Autowired
  private EntityManager entityManager;

  public AssignedModules getAssignedModules(UUID chwId) {
    return repository.findByHealthWorkerId(chwId);
  }

  /**
   * Assign modules to CHW.
   * @param assignedModules modules assigned for CHW
   */
  @Transactional
  public void assignModules(AssignedModules assignedModules) {
    AssignedModules existingAssignedModules =
        repository.findByHealthWorkerId(assignedModules.getHealthWorker().getId());
    List<String> oldIvrGroups = new ArrayList<String>();
    if (existingAssignedModules != null) {
      oldIvrGroups = getIvrGroupsFromModules(existingAssignedModules.getModules());
      existingAssignedModules.setModules(assignedModules.getModules());
    } else {
      existingAssignedModules = assignedModules;
    }

    repository.save(existingAssignedModules);

    entityManager.flush();
    entityManager.refresh(existingAssignedModules);

    String ivrId = existingAssignedModules.getHealthWorker().getIvrId();

    if (ivrId == null) {
      throw new ModuleAssignmentException(
          "Could not assign module to CHW, because CHW has empty IVR id");
    }

    try {
      ivrService.manageSubscriberGroups(ivrId,
          getIvrGroupsFromModules(existingAssignedModules.getModules()),
          oldIvrGroups);
    } catch (IvrException ex) {
      String message = "Could not assign or delete module for CHW, "
          + "because of IVR module assignment error.\n\n" + ex.getClearVotoInfo();
      throw new ModuleAssignmentException(message, ex);
    }
  }

  private List<String> getIvrGroupsFromModules(Set<Module> modules) {
    List<String> ivrGroups = new ArrayList<>();

    for (Module module: modules) {
      if (module.getIvrGroup() != null) {
        ivrGroups.add(module.getIvrGroup());
      }
    }

    return ivrGroups;
  }
}
