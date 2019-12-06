package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityRepositoryCustom {

  Page<Community> search(String communityName, String parentFacility, String sectorName,
      String districtName, Pageable pageable) throws IllegalArgumentException;
}
