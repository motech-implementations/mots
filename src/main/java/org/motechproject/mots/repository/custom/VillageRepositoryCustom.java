package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Village;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VillageRepositoryCustom {

  Page<Village> search(String villageName, String parentFacility, String sectorName,
      String districtName, Pageable pageable);
}
