package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DistrictRepositoryCustom {

  Page<District> search(String districtName, Pageable pageable);
}
