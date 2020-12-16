package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityHealthWorkerRepository extends
    JpaRepository<CommunityHealthWorker, UUID>, CommunityHealthWorkerRepositoryCustom {

  Optional<CommunityHealthWorker> findByChwId(String chwId);

  Optional<CommunityHealthWorker> findByPhoneNumber(String phoneNumber);

  Optional<CommunityHealthWorker> findByIvrId(String ivrId);

  List<CommunityHealthWorker> findByDistrictIdAndSelected(UUID districtId, Boolean selected);

  List<CommunityHealthWorker> findBySectorIdAndSelected(UUID sectorId, Boolean selected);

  List<CommunityHealthWorker> findByFacilityIdAndSelected(UUID facilityId, Boolean selected);

  List<CommunityHealthWorker> findByGroupIdAndSelected(UUID groupId, Boolean selected);

  List<CommunityHealthWorker> findBySelectedOrderByChwId(Boolean selected);
}
