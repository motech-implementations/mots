package org.motechproject.mots.service;

import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InchargeService {

  @Autowired
  private InchargeRepository inchargeRepository;

  public Iterable<Incharge> getIncharges() {
    return inchargeRepository.findAll();
  }

  public Incharge createIncharge(Incharge incharge) {
    return inchargeRepository.save(incharge);
  }
}
