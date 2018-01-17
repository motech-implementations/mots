package org.motechproject.mots.repository;

import java.util.Optional;
import java.util.UUID;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.springframework.data.repository.CrudRepository;

public interface CommunityHealthWorkerRepository extends
    CrudRepository<CommunityHealthWorker, UUID> {

  Optional<CommunityHealthWorker> findByChwId(String chwId);

  Optional<CommunityHealthWorker> findByPhoneNumber(String phoneNumber);
}
