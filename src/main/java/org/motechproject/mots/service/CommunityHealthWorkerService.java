package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.domain.AssignedModules;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.exception.ChwCreationException;
import org.motechproject.mots.exception.IvrException;
import org.motechproject.mots.repository.AssignedModulesRepository;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityHealthWorkerService {

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  @Autowired
  private AssignedModulesRepository assignedModulesRepository;

  @Autowired
  private IvrService ivrService;

  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Create CHW and IVR Subscriber, and assign it to CHW. Initiate empty AssignedModules
   * instance for newly created CHW.
   * @param healthWorker CHW to be created
   * @return saved CHW
   */
  public CommunityHealthWorker createHealthWorker(CommunityHealthWorker healthWorker) {
    String phoneNumber = healthWorker.getPhoneNumber();
    String name = healthWorker.getCombinedName();

    try {
      String ivrId = ivrService.createSubscriber(phoneNumber, name);
      healthWorker.setIvrId(ivrId);
    } catch (IvrException ex) {
      String message = "Could not create CHW, because of IVR subscriber creation error. " + ex.getClearVotoInfo();
      throw new ChwCreationException(message, ex);
    }

    healthWorkerRepository.save(healthWorker);

    AssignedModules emptyAssignedModulesInstance = new AssignedModules(healthWorker);
    assignedModulesRepository.save(emptyAssignedModulesInstance);

    return healthWorker;
  }

  public CommunityHealthWorker getHealthWorker(UUID id) {
    return healthWorkerRepository.findOne(id);
  }

  public CommunityHealthWorker saveHealthWorker(CommunityHealthWorker communityHealthWorker) {
    return healthWorkerRepository.save(communityHealthWorker);
  }
}
