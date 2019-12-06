package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Sector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SectorRepositoryCustom {

  Page<Sector> search(String sectorName, String parentDistrict, Pageable pageable)
      throws IllegalArgumentException;
}
