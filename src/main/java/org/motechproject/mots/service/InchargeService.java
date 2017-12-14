package org.motechproject.mots.service;

import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InchargeService {

  @Autowired
  private InchargeRepository inchargeRepository;

  /**
   * Creates Incharge.
   * @param incharge Incharge to be created
   * @return saved CHW
   */
  public Incharge createHealthWorker(Incharge incharge) {

    inchargeRepository.save(incharge);

    return incharge;
  }
}
