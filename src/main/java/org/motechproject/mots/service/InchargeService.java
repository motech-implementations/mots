package org.motechproject.mots.service;

import java.util.UUID;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.domain.security.UserPermission.RoleNames;
import org.motechproject.mots.exception.EntityNotFoundException;
import org.motechproject.mots.repository.InchargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    return inchargeRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Incharge with id: {0} not found", id.toString()));
  }

  /**
   * Finds Incharges matching all of the provided parameters.
   * If there are no parameters, return all Incharges.
   */
  @PreAuthorize(RoleNames.HAS_INCHARGE_READ_ROLE)
  public Page<Incharge> searchIncharges(String firstName, String secondName, String otherName,
      String phoneNumber, String email, String facilityName, Pageable pageable)
      throws IllegalArgumentException {

    return inchargeRepository.searchIncharges(firstName, secondName, otherName,
        phoneNumber, email, facilityName, pageable);
  }
}
