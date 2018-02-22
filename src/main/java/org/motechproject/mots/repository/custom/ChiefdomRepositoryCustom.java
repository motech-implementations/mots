package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Chiefdom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChiefdomRepositoryCustom {

  Page<Chiefdom> search(String chiefdomName, String parentDistrict, Pageable pageable)
      throws IllegalArgumentException;
}
