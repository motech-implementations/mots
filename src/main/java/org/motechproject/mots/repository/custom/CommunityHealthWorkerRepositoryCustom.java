package org.motechproject.mots.repository.custom;

import org.motechproject.mots.domain.CommunityHealthWorker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityHealthWorkerRepositoryCustom {

  Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String chiefdomId,
      String districtId, String communityId, Pageable pageable) throws IllegalArgumentException;
}
