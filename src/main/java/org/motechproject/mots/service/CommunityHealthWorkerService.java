package org.motechproject.mots.service;

import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.CommunityHealthWorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityHealthWorkerService {

  @Autowired
  private CommunityHealthWorkerRepository healthWorkerRepository;

  public Iterable<CommunityHealthWorker> getHealthWorkers() {
    return healthWorkerRepository.findAll();
  }

  public CommunityHealthWorker createHealthWorker(CommunityHealthWorker healthWorker) {
    return healthWorkerRepository.save(healthWorker);
  }
}
