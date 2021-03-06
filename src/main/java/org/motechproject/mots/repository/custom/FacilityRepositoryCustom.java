package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacilityRepositoryCustom {

  Page<Facility> search(String facilityName,
      String inchargeFullName, String inchargePhone, String inchargeEmail,
      String parentSector, String districtName, Pageable pageable);
}
