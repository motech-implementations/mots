package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityRepositoryCustom {

  Page<Facility> search(String facilityId, String facilityName, String facilityType,
      String inchargeFullName, String parentChiefdom, String districtName, Pageable pageable)
      throws IllegalArgumentException;
}
