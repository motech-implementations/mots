package org.motechproject.mots.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityHealthWorkerRepository extends
    JpaRepository<CommunityHealthWorker, UUID>, CommunityHealthWorkerRepositoryCustom {

  Optional<CommunityHealthWorker> findById(UUID id);

  Optional<CommunityHealthWorker> findByChwId(String chwId);

  Optional<CommunityHealthWorker> findByPhoneNumber(String phoneNumber);

  List<CommunityHealthWorker> findByVillageFacilitySectorDistrictIdAndSelected(UUID districtId,
      Boolean selected);

  List<CommunityHealthWorker> findByVillageFacilitySectorIdAndSelected(UUID sectorId,
      Boolean selected);

  // Adding @Query as the JPA query doesn't work for this case for some reason
  @Query("SELECT c FROM CommunityHealthWorker c WHERE c.village.facility.id = ?1 "
      + "AND c.selected = ?2")
  List<CommunityHealthWorker> findByVillageFacilityIdAndSelected(UUID facilityId,
      Boolean selected);

  List<CommunityHealthWorker> findByGroupIdAndSelected(UUID groupId, Boolean selected);

  List<CommunityHealthWorker> findBySelectedOrderByChwId(Boolean selected);
}
