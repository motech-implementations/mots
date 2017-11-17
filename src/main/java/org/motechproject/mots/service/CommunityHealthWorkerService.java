package org.motechproject.mots.service;

import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityHealthWorkerService {

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  @Autowired
  private IvrService ivrService;

  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  /**
   * Create CHW and IVR Subscriber and assign it to CHW.
   * @param healthWorker CHW to be created
   * @return saved CHW
   */
  public CommunityHealthWorker createHealthWorker(CommunityHealthWorker healthWorker) {
    String phoneNumber = healthWorker.getPhoneNumber();

    String ivrId = ivrService.createSubscriber(phoneNumber);
    healthWorker.setIvrId(ivrId);

    return healthWorkerRepository.save(healthWorker);
  }
}
