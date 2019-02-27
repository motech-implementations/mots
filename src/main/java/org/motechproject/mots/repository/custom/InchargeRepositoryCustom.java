package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Incharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InchargeRepositoryCustom {

  Page<Incharge> searchIncharges(String firstName, String secondName, String otherName,
      String phoneNumber, String email, String facilityName, String facilityId,
      String chiefdomName, String districtName, Boolean selected, Pageable pageable)
      throws IllegalArgumentException;
}
