package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class InchargeService {

  @Autowired
  private InchargeRepository inchargeRepository;

  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Iterable<Incharge> getIncharges() {
    return inchargeRepository.findAll();
  }

  @PreAuthorize(RoleNames.HAS_INCHARGE_WRITE_ROLE)
  public Incharge saveIncharge(Incharge incharge) {
    return inchargeRepository.save(incharge);
  }

  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Incharge getIncharge(UUID id) {
    return inchargeRepository.findOne(id);
  }
}
