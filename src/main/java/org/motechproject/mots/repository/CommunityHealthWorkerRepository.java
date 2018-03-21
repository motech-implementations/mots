package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityHealthWorkerRepository extends
    JpaRepository<CommunityHealthWorker, UUID>, CommunityHealthWorkerRepositoryCustom {

  Optional<CommunityHealthWorker> findById(UUID id);

  Optional<CommunityHealthWorker> findByChwId(String chwId);

  Optional<CommunityHealthWorker> findByPhoneNumber(String phoneNumber);

  List<CommunityHealthWorker> findByCommunityFacilityChiefdomDistrictIdAndSelected(UUID districtId,
      Boolean selected);

  List<CommunityHealthWorker> findBySelected(Boolean selected);
}
