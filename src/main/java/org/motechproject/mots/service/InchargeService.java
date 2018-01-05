package org.motechproject.mots.service;

import java.util.UUID;
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

  public Incharge saveIncharge(Incharge incharge) {
    return inchargeRepository.save(incharge);
  }

  public Incharge getIncharge(UUID id) {
    return inchargeRepository.findOne(id);
  }
}
